package com.hahazql.tools.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public abstract class ByteUtils {

    public static final long NULL_LENGTH = -1;

    public static byte[] readNullTerminatedBytes(byte[] data, int index) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = index; i < data.length; i++) {
            byte item = data[i];
            if (item == MSC.NULL_TERMINATED_STRING_DELIMITER) {
                break;
            }
            out.write(item);
        }
        return out.toByteArray();
    }

    public static void writeNullTerminatedString(String str, ByteArrayOutputStream out) throws IOException {
        out.write(str.getBytes());
        out.write(MSC.NULL_TERMINATED_STRING_DELIMITER);
    }

    public static void writeNullTerminated(byte[] data, ByteArrayOutputStream out) throws IOException {
        out.write(data);
        out.write(MSC.NULL_TERMINATED_STRING_DELIMITER);
    }

    public static byte[] readFixedLengthBytes(byte[] data, int index, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, index, bytes, 0, length);
        return bytes;
    }

    /**
     * Read 4 bytes in Little-endian byte order.
     *
     * @param data,  the original byte array
     * @param index, start to read from.
     * @return
     */
    public static long readUnsignedIntLittleEndian(byte[] data, int index) {
        long result = (long) (data[index] & 0xFF) | (long) ((data[index + 1] & 0xFF) << 8)
                | (long) ((data[index + 2] & 0xFF) << 16) | (long) ((data[index + 3] & 0xFF) << 24);
        return result;
    }

    public static long readUnsignedLongLittleEndian(byte[] data, int index) {
        long accumulation = 0;
        int position = index;
        for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
            accumulation |= (long) ((data[position++] & 0xff) << shiftBy);
        }
        return accumulation;
    }

    public static int readUnsignedShortLittleEndian(byte[] data, int index) {
        int result = (data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8);
        return result;
    }

    public static int readUnsignedMediumLittleEndian(byte[] data, int index) {
        int result = (data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8) | ((data[index + 2] & 0xFF) << 16);
        return result;
    }

    public static long readLengthCodedBinary(byte[] data, int index) throws IOException {
        int firstByte = data[index] & 0xFF;
        switch (firstByte) {
            case 251:
                return NULL_LENGTH;
            case 252:
                return readUnsignedShortLittleEndian(data, index + 1);
            case 253:
                return readUnsignedMediumLittleEndian(data, index + 1);
            case 254:
                return readUnsignedLongLittleEndian(data, index + 1);
            default:
                return firstByte;
        }
    }

    public static byte[] readBinaryCodedLengthBytes(byte[] data, int index) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(data[index]);

        byte[] buffer = null;
        int value = data[index] & 0xFF;
        if (value == 251) {
            buffer = new byte[0];
        }
        if (value == 252) {
            buffer = new byte[2];
        }
        if (value == 253) {
            buffer = new byte[3];
        }
        if (value == 254) {
            buffer = new byte[8];
        }
        if (buffer != null) {
            System.arraycopy(data, index + 1, buffer, 0, buffer.length);
            out.write(buffer);
        }

        return out.toByteArray();
    }

    public static void writeUnsignedIntLittleEndian(long data, ByteArrayOutputStream out) {
        out.write((byte) (data & 0xFF));
        out.write((byte) (data >>> 8));
        out.write((byte) (data >>> 16));
        out.write((byte) (data >>> 24));
    }

    public static void writeUnsignedShortLittleEndian(int data, ByteArrayOutputStream out) {
        out.write((byte) (data & 0xFF));
        out.write((byte) ((data >>> 8) & 0xFF));
    }

    public static void writeUnsignedMediumLittleEndian(int data, ByteArrayOutputStream out) {
        out.write((byte) (data & 0xFF));
        out.write((byte) ((data >>> 8) & 0xFF));
        out.write((byte) ((data >>> 16) & 0xFF));
    }

    public static void writeBinaryCodedLengthBytes(byte[] data, ByteArrayOutputStream out) throws IOException {
        // 1. write length byte/bytes
        if (data.length < 252) {
            out.write((byte) data.length);
        } else if (data.length < (1 << 16L)) {
            out.write((byte) 252);
            writeUnsignedShortLittleEndian(data.length, out);
        } else if (data.length < (1 << 24L)) {
            out.write((byte) 253);
            writeUnsignedMediumLittleEndian(data.length, out);
        } else {
            out.write((byte) 254);
            writeUnsignedIntLittleEndian(data.length, out);
        }
        // 2. write real data followed length byte/bytes
        out.write(data);
    }

    public static void writeFixedLengthBytes(byte[] data, int index, int length, ByteArrayOutputStream out) {
        for (int i = index; i < index + length; i++) {
            out.write(data[i]);
        }
    }

    public static void writeFixedLengthBytesFromStart(byte[] data, int length, ByteArrayOutputStream out) {
        writeFixedLengthBytes(data, 0, length, out);
    }


    public static void putLong(byte[] b, int off, long val) {
        b[off + 7] = (byte) (val >>> 0);
        b[off + 6] = (byte) (val >>> 8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off + 0] = (byte) (val >>> 56);
    }


    public static final int getLengthWithBytes(byte[] src) {
        int length = src.length;
        if (length < 251) {
            return 1 + length;
        } else if (length < 0x10000L) {
            return 3 + length;
        } else if (length < 0x1000000L) {
            return 4 + length;
        } else {
            return 9 + length;
        }
    }

    public static final byte[] getBytesWithLength(int length) {
        byte[] bb = null;
        if (length < 251) {
            bb = new byte[1];
            bb[0] = (byte) length;
        } else if (length < 0x10000L) {
            bb = new byte[3];
            bb[0] = (byte) 252;
            bb[1] = (byte) (length & 0xff);
            bb[2] = (byte) (length >>> 8);
        } else if (length < 0x1000000L) {
            bb = new byte[4];
            bb[0] = (byte) 253;
            bb[1] = (byte) (length & 0xff);
            bb[2] = (byte) (length >>> 8);
            bb[3] = (byte) (length >>> 16);
        } else {
            bb = new byte[9];
            bb[0] = (byte) 254;
            bb[1] = (byte) (length & 0xff);
            bb[2] = (byte) (length >>> 8);
            bb[3] = (byte) (length >>> 16);
            bb[4] = (byte) (length >>> 24);
            bb[5] = (byte) (length >>> 32);
            bb[6] = (byte) (length >>> 40);
            bb[7] = (byte) (length >>> 48);
            bb[8] = (byte) (length >>> 56);
        }
        return bb;
    }

}
