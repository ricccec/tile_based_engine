package net;

import java.nio.ByteBuffer;

public class PacketWriter {
    private final ByteBuffer buffer;

    public PacketWriter(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void writeInt(int value) {
        buffer.putInt(value);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes();
        buffer.putInt(bytes.length);
        buffer.put(bytes);
    }

    public void writeBoolean(boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }
}