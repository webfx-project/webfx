/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package emul.java.nio;

import java.lang.Comparable;

/**
 *
 * @author Alexey Andreev
 */
public abstract class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {
    int start;
    byte[] array;
    emul.java.nio.ByteOrder order = emul.java.nio.ByteOrder.BIG_ENDIAN;

    ByteBuffer(int start, int capacity, byte[] array, int position, int limit) {
        super(capacity);
        this.start = start;
        this.array = array;
        this.position = position;
        this.limit = limit;
    }

    public static ByteBuffer allocateDirect(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity is negative: " + capacity);
        }
        return new ByteBufferImpl(capacity, true);
    }

    public static ByteBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity is negative: " + capacity);
        }
        return new ByteBufferImpl(capacity, false);
    }

    public static ByteBuffer wrap(byte[] array, int offset, int length) {
        return new ByteBufferImpl(0, array.length, array, offset, offset + length, false, false);
    }

    public static ByteBuffer wrap(byte[] array) {
        return wrap(array, 0, array.length);
    }

    public abstract ByteBuffer slice();

    public abstract ByteBuffer duplicate();

    public abstract ByteBuffer asReadOnlyBuffer();

    public abstract byte get();

    public abstract ByteBuffer put(byte b);

    public abstract byte get(int index);

    public abstract ByteBuffer put(int index, byte b);

    public ByteBuffer get(byte[] dst, int offset, int length) {
        if (offset < 0 || offset >= dst.length) {
            throw new IndexOutOfBoundsException("Offset " + offset + " is outside of range [0;" + dst.length + ")");
        }
        if (offset + length > dst.length) {
            throw new IndexOutOfBoundsException("The last byte in dst " + (offset + length) + " is outside "
                    + "of array of size " + dst.length);
        }
        if (remaining() < length) {
            throw new emul.java.nio.BufferUnderflowException();
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("Length " + length + " must be non-negative");
        }
        int pos = position + start;
        for (int i = 0; i < length; ++i) {
            dst[offset++] = array[pos++];
        }
        position += length;
        return this;
    }

    public ByteBuffer get(byte[] dst) {
        return get(dst, 0, dst.length);
    }

    public ByteBuffer put(ByteBuffer src) {
        return put(src.array, src.start + src.position, src.remaining());
    }

    public ByteBuffer put(byte[] src, int offset, int length) {
        if (length == 0) {
            return this;
        }
        if (isReadOnly()) {
            throw new emul.java.nio.ReadOnlyBufferException();
        }
        if (remaining() < length) {
            throw new emul.java.nio.BufferOverflowException();
        }
        if (offset < 0 || offset >= src.length) {
            throw new IndexOutOfBoundsException("Offset " + offset + " is outside of range [0;" + src.length + ")");
        }
        if (offset + length > src.length) {
            throw new IndexOutOfBoundsException("The last byte in src " + (offset + length) + " is outside "
                    + "of array of size " + src.length);
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("Length " + length + " must be non-negative");
        }
        int pos = position + start;
        for (int i = 0; i < length; ++i) {
            array[pos++] = src[offset++];
        }
        position += length;
        return this;
    }

    public final ByteBuffer put(byte[] src) {
        return put(src, 0, src.length);
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public final byte[] array() {
        return array;
    }

    @Override
    public int arrayOffset() {
        return start;
    }

    public abstract ByteBuffer compact();

    @Override
    public abstract boolean isDirect();

    @Override
    public String toString() {
        return "[ByteBuffer position=" + position + ", limit=" + limit + ", capacity=" + capacity + ", mark "
                + (mark >= 0 ? " at " + mark : " is not set") + "]";
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        int pos = position + start;
        for (int i = position; i < limit; ++i) {
            hashCode = 31 * hashCode + array[pos++];
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ByteBuffer)) {
            return false;
        }
        ByteBuffer other = (ByteBuffer) obj;
        int sz = remaining();
        if (sz != other.remaining()) {
            return false;
        }
        int a = position + start;
        int b = other.position + other.start;
        for (int i = 0; i < sz; ++i) {
            if (array[a++] != other.array[b++]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(ByteBuffer other) {
        if (this == other) {
            return 0;
        }
        int sz = Math.min(remaining(), other.remaining());
        int a = position + start;
        int b = other.position + other.start;
        for (int i = 0; i < sz; ++i) {
            byte x = array[a++];
            byte y = other.array[b++];
            int r = x - y;
            if (r != 0) {
                return r;
            }
        }
        int x = remaining();
        int y = other.remaining();
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public final emul.java.nio.ByteOrder order() {
        return order;
    }

    public final ByteBuffer order(emul.java.nio.ByteOrder bo) {
        order = bo;
        return this;
    }

    public abstract char getChar();

    public abstract ByteBuffer putChar(char value);

    public abstract char getChar(int index);

    public abstract ByteBuffer putChar(int index, char value);

    //public abstract CharBuffer asCharBuffer();

    public abstract short getShort();

    public abstract ByteBuffer putShort(short value);

    public abstract short getShort(int index);

    public abstract ByteBuffer putShort(int index, short value);

    //public abstract ShortBuffer asShortBuffer();

    public abstract int getInt();

    public abstract ByteBuffer putInt(int value);

    public abstract int getInt(int index);

    public abstract ByteBuffer putInt(int index, int value);

    public abstract emul.java.nio.IntBuffer asIntBuffer();

    public abstract long getLong();

    public abstract ByteBuffer putLong(long value);

    public abstract long getLong(int index);

    public abstract ByteBuffer putLong(int index, long value);

    //public abstract LongBuffer asLongBuffer();

    //public abstract FloatBuffer asFloatBuffer();

    //public abstract DoubleBuffer asDoubleBuffer();
}
