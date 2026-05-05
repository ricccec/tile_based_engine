package net;

import java.nio.ByteBuffer;

public class PacketReader {
    private final ByteBuffer buffer;

    public PacketReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public int readInt() {
        return buffer.getInt();
    }

    public String readString() {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    public boolean readBoolean() {
        return buffer.get() != 0;
    }
}