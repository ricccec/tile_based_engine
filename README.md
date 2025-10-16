# mmorpg_engine

Built in Java, this **custom 2D tile‑based retro engine** is a self‑learning project to explore how 2D engines are made. To keep it engaging, it’s developed in tandem with a Gen‑2 Pokémon–inspired game, with a long‑term goal of supporting MMORPG features.

It currently includes a tiled overworld, grid‑based physics, sprite rendering, a HUD and dialog system, zones and triggers, and an event‑driven interaction model.

![preview_2d_engine](https://github.com/user-attachments/assets/347a403d-7864-460b-9d73-10125c89d19d)

## Features

- Tiles defined via JSON with tilesets, frames, and animations
- Grid-bound physics with cell-based collision and obstacles
- Sprite-based rendering with states, directions, and animations
- Event-driven interaction system (controls, text/dialog, scripted events)
- Zones and triggers (jump zones, trigger zones, platform obstacles)
- HUD stack with dialog text rendering and input handling
- Simple demo launcher and NPC spawning for quick iteration

## Repository layout

- Build and wrappers: [build.gradle](build.gradle), [settings.gradle](settings.gradle), [gradlew](gradlew), [gradlew.bat](gradlew.bat), [gradle/wrapper/gradle-wrapper.properties](gradle/wrapper/gradle-wrapper.properties)
- Source:
  - Engine code: [src/main/java](src/main/java)
  - Tests: [src/test/java](src/test/java)
- Sample input/assets (JSON, images): [input/](input/), [immagini/](immagini/)

Run the demo (from IDE):
- Open and run the `main` method in [`pokemon_online.GameTester`](src/main/java/pokemon_online/GameTester.java)

Note:
- Overworld maps and graphics are loaded via [`pokemon_online.ResourcesManager`](src/main/java/pokemon_online/ResourcesManager.java), which scans under a resources directory (see Configuration in code).

## Quick demo snippet

Create a simple NPC with a sprite and a dialog, then spawn it into the current map.

````java
// Instantiate a test application
GameTester tester = new GameTester();

// Create a player object with overworld map and spawning grid cell
tester.setPlayerSprite("F Allenatrice");
tester.jumpToLand("Pokecity", 9, 9);

// Create an NPC with a sprite and text interaction
GameObject npc = new GameObject();
SpriteGraphicsComponent g = new SpriteGraphicsComponent(npc);
SpriteData data = ResourcesManager.getMgr().getGameObjectGraphics("F Allenatrice");
data.initSpriteGrapComponent(g);
npc.setGraphicsComponent(g);
npc.setInteractionComponent(new InteractionComponent(npc));
npc.getInteractionComponent().addEventHandler(new TextEventHandler("Hello there!"));

// Spawn via the tester helper into a specific grid cell
tester.spawnObject(npc, 9, 10);

// Start the game loop and render
tester.startGameLoop();		
tester.setVisible(true);
