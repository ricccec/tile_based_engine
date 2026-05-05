# MMO Multi-Server Architecture

## Overview

This document describes a scalable, fault-tolerant multi-server architecture for an MMO. The system separates authentication, world simulation, combat, and persistence while avoiding central bottlenecks and ensuring server-authoritative gameplay.

Correctness in distributed state is enforced through **ownership leases** and **fencing tokens** — not by isolating storage per service. The database is a shared, logically unified persistence layer; servers are the consistency boundary.

---

## Core Principles

- **Separation of concerns** — each service has a single, well-defined responsibility
- **Stateless authentication** — signed tokens validated locally, no runtime calls to the Auth Server
- **Server-authoritative model** — clients never decide game state
- **Single ownership of runtime state** — exactly one server is authoritative for any piece of state at any moment, enforced via leases and fencing tokens
- **Logically unified persistence** — one Game DB shared by all services; ownership controls writes, not storage isolation
- **Time-bounded authority** — ownership is always held under a lease with expiry; zombie servers cannot corrupt state

---

## Components

### 1. Auth Server

Responsible for:
- User authentication (email/password)
- Issuing **signed session tokens** (HMAC or RSA)

Stores:
- `userId` (primary key)
- Email
- Password hash

Notes:
- Does **not** participate in gameplay
- Does **not** validate tokens at runtime — validation is done locally by each server

---

### 2. Gateway / Entry Service

Responsible for:
- Validating session tokens
- Loading the player's last known zone from the Game DB
- Mapping zone → World Server via the Zone Registry
- Returning the correct World Server address to the client

---

### 3. World Servers (Overworld / Zones)

Responsible for:
- Player movement and position enforcement
- World simulation
- Zone transition coordination

Own (in-memory, authoritative, under lease):
- Position (X, Y)
- Current zone
- Movement state

Each World Server holds a **lease** on each player it owns. State kept in memory must be flushed to the Game DB on zone transitions, logout, and periodic checkpoints.

---

### 4. Battle Servers

Responsible for:
- Combat simulation
- Turn resolution

Own (in-memory, authoritative, under lease):
- Party state
- HP, status effects
- Battle progression

Battle Servers are spun up dynamically as needed. When a player enters battle, the World Server locks the player and transfers ownership to a Battle Server (see Ownership Transfer below).

---

### 5. Persistence Layer (Game DB)

Responsible for:
- Long-term player data
- Ownership and lease metadata
- Event log for recovery

Schema (minimum viable):

```sql
CREATE TABLE players (
  player_id        UUID PRIMARY KEY,
  owner_server_id  VARCHAR,
  fence_token      BIGINT NOT NULL DEFAULT 0,
  lease_expires_at TIMESTAMPTZ,
  transfer_status  VARCHAR,        -- NULL | 'transferring'
  last_zone        VARCHAR,
  position_x       FLOAT,
  position_y       FLOAT,
  inventory        JSONB,
  progression      JSONB
);
```

Notes:
- **Single logical source of truth** — not split across services
- All servers read from and write to this DB, but only for players they currently own

---

## Identity Model

### userId

- Unique, immutable identifier assigned at registration
- Shared between Auth DB and Game DB
- Used as the internal key everywhere

❌ Do **not** use email as an identifier  
✅ Use `userId` everywhere internally

---

## Authentication Model

### Token-Based Authentication

The Auth Server issues a **signed token** on login:

```json
{
  "sub": "userId",
  "exp": 1710000000
}
```

- Signed (HMAC or RSA); not encrypted
- Contains no sensitive data (e.g. no email)

### Token Validation

Each server validates the token **locally** by verifying its signature and extracting `userId`.

✅ No runtime call to the Auth Server required

---

## Player Session Flow

### 1. Login

```
Client → Auth Server
       ← signed token
```

### 2. Discover World Server

```
Client → Gateway (sends token)
```

Gateway:
1. Validates token signature locally
2. Loads player from Game DB, reads `last_zone`
3. Looks up zone → World Server in the Zone Registry
4. Returns connection info

```json
{
  "serverIp": "...",
  "port": 12345,
  "zone": "zone_12"
}
```

### 3. Connect to World Server

Client connects to the returned address. The World Server:
1. Validates the token locally
2. Claims ownership of the player via a compare-and-swap (see Ownership section)
3. Loads player state from Game DB

### First Login Handling

**New player** — no record in Game DB (`last_zone = NULL`):
- Server creates the player record with `last_zone = STARTING_AREA`, `position = (spawnX, spawnY)`

**Returning player** — server loads `last_zone` and `position` from DB.

The **server** always determines spawn location from DB state. The client never proposes a location.

---

## Ownership, Leases, and Fencing Tokens

This is the core correctness mechanism. Multiple servers share one DB, so writes must be strictly controlled.

### The Ownership Model

Each player row carries:

| Field | Purpose |
|---|---|
| `owner_server_id` | Which server is currently authoritative |
| `fence_token` | Monotonically increasing integer; incremented on every ownership change |
| `lease_expires_at` | Timestamp after which ownership has lapsed |
| `transfer_status` | `NULL` during normal operation, `'transferring'` during handoff |

A server is authoritative for a player if and only if:
- `owner_server_id = self`
- `fence_token = my_token` (the token issued when ownership was granted)
- `lease_expires_at > now()`

### Writing State

Every write is conditional — never a blind UPDATE:

```sql
UPDATE players
SET    hp = $new_hp,
       lease_expires_at = now() + interval '10 seconds'
WHERE  player_id        = $id
  AND  owner_server_id  = $my_server_id
  AND  fence_token      = $my_token
  AND  lease_expires_at > now();
```

**If `rows_affected = 0`, the server has lost its lease and must immediately stop processing that player.** This check is mandatory on every write.

### Lease Renewal

Owners renew their lease before it expires:

```sql
UPDATE players
SET    lease_expires_at = now() + interval '10 seconds'
WHERE  player_id        = $id
  AND  owner_server_id  = $my_server_id
  AND  fence_token      = $my_token
  AND  lease_expires_at > now();
```

Lease TTL is typically 5–30 seconds. Shorter means faster failover but more renewal traffic.

### Claiming an Expired Lease

When a server wants to take ownership after a crash or reconnect:

```sql
UPDATE players
SET    owner_server_id  = $my_id,
       fence_token      = fence_token + 1,
       lease_expires_at = now() + interval '10 seconds'
WHERE  player_id        = $id
  AND  (owner_server_id IS NULL OR lease_expires_at < now());
```

The server that gets `rows_affected = 1` is the new owner and receives the new `fence_token` value. The DB serializes concurrent attempts — only one wins.

### Why Both Leases and Fencing Tokens

| | Leases | Fencing Tokens |
|---|---|---|
| Protects against | Permanently stuck ownership after a crash | Zombie servers writing with stale belief |
| Relies on | Clocks (can drift) | Monotonic counter (always correct) |
| Failover speed | Lease TTL duration | Immediate — new token is already issued |

Used together, they cover all failure modes. Leases alone are not sufficient because a server that is paused (GC, VM migration) can wake up unaware that its lease expired.

---

## Runtime State vs. Persistence

### Runtime State (distributed, server-owned)

| Server | Owns |
|---|---|
| World Server | Position, movement state, zone |
| Battle Server | Combat state, HP, status effects, battle progression |

- Kept in memory; authoritative during gameplay
- Must be flushed to Game DB on transition, logout, or checkpoint
- Lost on crash — recovered from DB on restart

### Persistent State (Game DB)

- Player progression, inventory, location, ownership metadata
- Single source of truth; survives any individual server failure

**Rule:** Only one server owns a piece of runtime state at any time, enforced through the lease + fencing token mechanism.

---

## Zone Registry

The system maintains a mapping of zone → World Server:

```
zone_1 → server_A
zone_2 → server_B
zone_3 → server_B
```

Used by:
- **Gateway** — for initial player routing
- **World Servers** — to determine the target server on zone transitions

---

## Ownership Transfer (Zone Transitions and Battle Entry)

Ownership transfers cover two scenarios: planned zone-to-zone handoffs between two live World Servers, and World Server → Battle Server transfers.

### The Four-Phase Handoff Protocol

The goal is to eliminate two failure modes:
- **Gap** — a moment where neither server accepts writes
- **Overlap** — both servers accept writes simultaneously

```
1. PREPARE   Server A (outgoing) marks transfer_status = 'transferring',
             flushes all pending in-memory state to DB, stops accepting writes.

2. GRANT     Server B (incoming) claims ownership via compare-and-swap:
             SET owner = B, fence_token = fence_token + 1
             WHERE owner = A AND transfer_status = 'transferring'
             B now holds the new fence_token.

3. CONFIRM   B loads full player state from DB, begins accepting writes
             and client connections.

4. RELEASE   A receives confirmation, discards local player state.
             If confirmation never arrives, A polls the DB —
             once owner = B, the transfer is complete.
```

### Edge Cases

**Server A crashes during PREPARE** — the player is stuck in `'transferring'` with no active owner. The lease will expire naturally; a watchdog process or Server B can then claim ownership via the expired-lease path.

**Client disconnects mid-handoff** — complete the transfer anyway. When the client reconnects, it queries the Gateway for the current owner and connects to whoever that is.

**A and B cannot reach each other** — this doesn't matter. Servers never communicate directly during a handoff; the DB is the sole coordination point.

### Handoff Token (Zone Transitions)

To prevent clients from spoofing zone transitions, the outgoing server issues a short-lived handoff token:

```json
{
  "userId": "...",
  "fromZone": "zone_12",
  "toZone": "zone_13",
  "exp": 1710000060
}
```

The incoming server validates both the main session token (identity) and the handoff token (authorized transition) before accepting the connection.

### Border Zone Pre-loading (Latency Optimization)

When a player enters the border area of Zone A approaching Zone B, Server B begins receiving the player's state in read-only observer mode. It pre-loads inventory and caches whatever it needs. When the handoff fires, Server B is ready and the gap is near-zero from the player's perspective. This is a latency optimization layered on top of the correctness protocol — correctness is enforced by the protocol; this only reduces perceived latency.

---

## Recovery and Reconciliation

Recovery is the process of returning to a known-good state after a failure. The source of truth is always the Game DB.

### When Recovery Is Triggered

- A server's write returns `rows_affected = 0` (lost lease)
- A server restarts after a crash
- A handoff is found incomplete (player stuck in `'transferring'`)
- A network partition heals

### Recovery Steps (Lost Lease)

1. **STOP** — halt all writes for affected players immediately
2. **DIFF** — compare in-memory state against DB
3. **DECIDE** — apply field-level resolution rules (see below)
4. **DISCARD** — drop local state and reload from DB, or yield to the new owner

### Field-Level Resolution Rules

| Field | Strategy | Reason |
|---|---|---|
| Position | DB wins | New owner may have moved the player |
| HP (damage taken) | Take the lower value | Conservative; prevents damage from being lost |
| XP / currency | Requires event log | Cannot safely merge without knowing what was credited |
| Inventory additions | Idempotent via event log | Replay the event; DB constraint prevents duplicates |
| Quest progress | DB wins | New server may have advanced it |

### Recovering an Interrupted Handoff

A player stuck in `'transferring'` with the outgoing server gone:
1. Check whether PREPARE completed (state fully flushed, no dirty fields)
2. Check the fence token — was a new one issued?
3. Roll forward (complete the transfer) or roll back (return player to `owner = NULL`, let next reconnect claim via expired-lease path)

### Design for Recoverability

- **Flush frequently** — minimize the gap between in-memory state and DB
- **Log every operation** — an event log lets you reconstruct intent, not just final state
- **Make handlers idempotent** — replaying an event twice gives the same result as once
- **Treat servers as stateless processors** — the more a server treats itself as a cache of DB truth, the simpler recovery becomes

---

## Server-to-Server Communication

Servers communicate with each other using internal authentication — never user session tokens.

Options:
- **mTLS** — mutual TLS with per-service certificates
- **Service tokens** — short-lived signed tokens issued to each service

Trust model:
- Servers trust servers
- Servers do **not** trust clients

---

## Scalability

- Add World Servers horizontally; assign multiple zones per server via the Zone Registry
- Spin up Battle Servers dynamically on demand
- The Game DB is the only shared state; its scalability determines overall system limits — consider a distributed SQL database (CockroachDB, Spanner) if single-node DB becomes a bottleneck

---

## Key Design Decisions

| Decision | Rejected | Chosen |
|---|---|---|
| Token validation | Every server calls Auth Server | Local signature verification |
| Location authority | Client decides position | Server enforces all transitions |
| DB architecture | Per-service databases | Unified Game DB |
| Consistency boundary | Storage isolation | Ownership + lease + fencing token |
| Identity key | Email | `userId` |
| Ownership duration | Permanent owner field | Time-bounded lease with fencing token |

---

## Summary

This architecture provides:

- **Scalable world simulation** via horizontal World Server scaling and the Zone Registry
- **Secure, stateless authentication** via locally-validated signed tokens
- **Clear, enforced ownership** of all runtime state via leases and fencing tokens
- **Reliable persistence** in a single logical Game DB
- **Cheat-resistant transitions** via server-authoritative handoff protocol and handoff tokens
- **Fault tolerance** via lease expiry, watchdog recovery, and DB-as-coordinator handoffs
