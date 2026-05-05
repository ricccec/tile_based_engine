package main_server.net;

public class Session {
    boolean authenticated = false;
    long lastSeen = System.currentTimeMillis();
    int failedAttempts = 0;
}
