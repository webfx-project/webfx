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
package javaemul.nio;

/**
 *
 * @author Alexey Andreev
 */
class ByteBufferImpl extends ByteBuffer {
    private boolean direct;
    private boolean readOnly;

    public ByteBufferImpl(int capacity, boolean direct) {
        this(0, capacity, new byte[capacity], 0, capacity, direct, false);
    }

    public ByteBufferImpl(int start, int capacity, byte[] array, int position, int limit,
                           boolean direct, boolean readOnly) {
        super(start, capacity, array, position, limit);
        this.direct = direct;
        this.readOnly = readOnly;
    }

    @Override
    public ByteBuffer slice() {
        return new ByteBufferImpl(position, limit - position,  array, 0, limit - position, direct, readOnly);
    }

    @Override
    public ByteBuffer duplicate() {
        return new ByteBufferImpl(start, capacity, array, position, limit, direct, readOnly);
    }

    @Override
    public ByteBuffer asReadOnlyBuffer() {
        return new ByteBufferImpl(start, capacity, array, position, limit, direct, true);
    }

    @Override
    public byte get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        return array[start + position++];
    }

    @Override
    public ByteBuffer put(byte b) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        array[start + position++] = b;
        return this;
    }

    @Override
    public byte get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + limit + ")");
        }
        return array[start + index];
    }

    @Override
    public ByteBuffer put(int index, byte b) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + limit + ")");
        }
        array[start + index] = b;
        return this;
    }

    @Override
    public ByteBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position > 0) {
            int sz = remaining();
            int dst = start;
            int src = start + position;
            for (int i = 0; i < sz; ++i) {
                array[dst++] = array[src++];
            }
            position = sz;
        }
        limit = capacity;
        mark = -1;
        return this;
    }

    @Override
    public boolean isDirect() {
        return direct;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public char getChar() {
        if (position + 1 >= limit) {
            throw new BufferUnderflowException();
        }
        int a = array[start + position] & 0xFF;
        int b = array[start + position + 1] & 0xFF;
        position += 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (char) ((a << 8) | b);
        } else {
            return (char) ((b << 8) | a);
        }
    }

    @Override
    public ByteBuffer putChar(char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position + 1 >= limit) {
            throw new BufferOverflowException();
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) value;
        } else {
            array[start + position++] = (byte) value;
            array[start + position++] = (byte) (value >> 8);
        }
        return this;
    }

    @Override
    public char getChar(int index) {
        if (index < 0 || index + 1 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 1) + ")");
        }
        int a = array[start + index] & 0xFF;
        int b = array[start + index + 1] & 0xFF;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (char) ((a << 8) | b);
        } else {
            return (char) ((b << 8) | a);
        }
    }

    @Override
    public ByteBuffer putChar(int index, char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 1 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 1) + ")");
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + index] = (byte) (value >> 8);
            array[start + index + 1] = (byte) value;
        } else {
            array[start + index] = (byte) value;
            array[start + index + 1] = (byte) (value >> 8);
        }
        return this;
    }

    /* @Override
    public TCharBuffer asCharBuffer() {
        int sz = remaining() / 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return new CharBufferOverByteBufferBigEndian(start + position, sz, this, 0, sz, isReadOnly());
        } else {
            return new CharBufferOverByteBufferLittleEndian(start + position, sz, this, 0, sz, isReadOnly());
        }
    }*/

    @Override
    public short getShort() {
        if (position + 1 >= limit) {
            throw new BufferUnderflowException();
        }
        int a = array[start + position] & 0xFF;
        int b = array[start + position + 1] & 0xFF;
        position += 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((a << 8) | b);
        } else {
            return (short) ((b << 8) | a);
        }
    }

    @Override
    public ByteBuffer putShort(short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position + 1 >= limit) {
            throw new BufferOverflowException();
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) value;
        } else {
            array[start + position++] = (byte) value;
            array[start + position++] = (byte) (value >> 8);
        }
        return this;
    }

    @Override
    public short getShort(int index) {
        if (index < 0 || index + 1 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 1) + ")");
        }
        int a = array[start + index] & 0xFF;
        int b = array[start + index + 1] & 0xFF;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((a << 8) | b);
        } else {
            return (short) ((b << 8) | a);
        }
    }

    @Override
    public ByteBuffer putShort(int index, short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 1 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 1) + ")");
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + index] = (byte) (value >> 8);
            array[start + index + 1] = (byte) value;
        } else {
            array[start + index] = (byte) value;
            array[start + index + 1] = (byte) (value >> 8);
        }
        return this;
    }

    /*@Override
    public ShorBuffer asShorBuffer() {
        int sz = remaining() / 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return new TShorBufferOverByteBufferBigEndian(start + position, sz, this, 0, sz, isReadOnly());
        } else {
            return new TShorBufferOverByteBufferLittleEndian(start + position, sz, this, 0, sz, isReadOnly());
        }
    }*/

    @Override
    public int getInt() {
        if (position + 3 >= limit) {
            throw new BufferUnderflowException();
        }
        int a = array[start + position] & 0xFF;
        int b = array[start + position + 1] & 0xFF;
        int c = array[start + position + 2] & 0xFF;
        int d = array[start + position + 3] & 0xFF;
        position += 4;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (a << 24) | (b << 16) | (c << 8) | d;
        } else {
            return (d << 24) | (c << 16) | (b << 8) | a;
        }
    }

    @Override
    public ByteBuffer putInt(int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position + 3 >= limit) {
            throw new BufferOverflowException();
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + position++] = (byte) (value >> 24);
            array[start + position++] = (byte) (value >> 16);
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) value;
        } else {
            array[start + position++] = (byte) value;
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) (value >> 16);
            array[start + position++] = (byte) (value >> 24);
        }
        return this;
    }

    @Override
    public int getInt(int index) {
        if (index < 0 || index + 3 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 3) + ")");
        }
        int a = array[start + index] & 0xFF;
        int b = array[start + index + 1] & 0xFF;
        int c = array[start + index + 2] & 0xFF;
        int d = array[start + index + 3] & 0xFF;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (a << 24) | (b << 16) | (c << 8) | d;
        } else {
            return (d << 24) | (c << 16) | (b << 8) | a;
        }
    }

    @Override
    public ByteBuffer putInt(int index, int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 3 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 3) + ")");
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + index] = (byte) (value >> 24);
            array[start + index + 1] = (byte) (value >> 16);
            array[start + index + 2] = (byte) (value >> 8);
            array[start + index + 3] = (byte) value;
        } else {
            array[start + index] = (byte) value;
            array[start + index + 1] = (byte) (value >> 8);
            array[start + index + 2] = (byte) (value >> 16);
            array[start + index + 3] = (byte) (value >> 24);
        }
        return this;
    }

    @Override
    public IntBuffer asIntBuffer() {
        int sz = remaining() / 4;
        if (order == ByteOrder.BIG_ENDIAN) {
            return new IntBufferOverByteBufferBigEndian(start + position, sz, this, 0, sz, isReadOnly());
        } else {
            return new IntBufferOverByteBufferLittleEndian(start + position, sz, this, 0, sz, isReadOnly());
        }
    }

    @Override
    public long getLong() {
        if (position + 7 >= limit) {
            throw new BufferUnderflowException();
        }
        long a = array[start + position] & 0xFF;
        long b = array[start + position + 1] & 0xFF;
        long c = array[start + position + 2] & 0xFF;
        long d = array[start + position + 3] & 0xFF;
        long e = array[start + position + 4] & 0xFF;
        long f = array[start + position + 5] & 0xFF;
        long g = array[start + position + 6] & 0xFF;
        long h = array[start + position + 7] & 0xFF;
        position += 8;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (a << 56) | (b << 48) | (c << 40) | (d << 32) | (e << 24) | (f << 16) | (g << 8) | h;
        } else {
            return (h << 56) | (g << 48) | (f << 40) | (e << 32) | (d << 24) | (c << 16) | (b << 8) | a;
        }
    }

    @Override
    public ByteBuffer putLong(long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position + 7 >= limit) {
            throw new BufferOverflowException();
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + position++] = (byte) (value >> 56);
            array[start + position++] = (byte) (value >> 48);
            array[start + position++] = (byte) (value >> 40);
            array[start + position++] = (byte) (value >> 32);
            array[start + position++] = (byte) (value >> 24);
            array[start + position++] = (byte) (value >> 16);
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) value;
        } else {
            array[start + position++] = (byte) value;
            array[start + position++] = (byte) (value >> 8);
            array[start + position++] = (byte) (value >> 16);
            array[start + position++] = (byte) (value >> 24);
            array[start + position++] = (byte) (value >> 32);
            array[start + position++] = (byte) (value >> 40);
            array[start + position++] = (byte) (value >> 48);
            array[start + position++] = (byte) (value >> 56);
        }
        return this;
    }

    @Override
    public long getLong(int index) {
        if (index < 0 || index + 7 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 7) + ")");
        }
        long a = array[start + index] & 0xFF;
        long b = array[start + index + 1] & 0xFF;
        long c = array[start + index + 2] & 0xFF;
        long d = array[start + index + 3] & 0xFF;
        long e = array[start + index + 4] & 0xFF;
        long f = array[start + index + 5] & 0xFF;
        long g = array[start + index + 6] & 0xFF;
        long h = array[start + index + 7] & 0xFF;
        position += 8;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (a << 56) | (b << 48) | (c << 40) | (d << 32) | (e << 24) | (f << 16) | (g << 8) | h;
        } else {
            return (h << 56) | (g << 48) | (f << 40) | (e << 32) | (d << 24) | (c << 16) | (b << 8) | a;
        }
    }

    @Override
    public ByteBuffer putLong(int index, long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 3 >= limit) {
            throw new IndexOutOfBoundsException("Index " + index + " is outside of range [0;" + (limit - 3) + ")");
        }
        if (order == ByteOrder.BIG_ENDIAN) {
            array[start + index + 0] = (byte) (value >> 56);
            array[start + index + 1] = (byte) (value >> 48);
            array[start + index + 2] = (byte) (value >> 40);
            array[start + index + 3] = (byte) (value >> 32);
            array[start + index + 4] = (byte) (value >> 24);
            array[start + index + 5] = (byte) (value >> 16);
            array[start + index + 6] = (byte) (value >> 8);
            array[start + index + 7] = (byte) value;
        } else {
            array[start + index + 0] = (byte) value;
            array[start + index + 1] = (byte) (value >> 8);
            array[start + index + 2] = (byte) (value >> 16);
            array[start + index + 3] = (byte) (value >> 24);
            array[start + index + 4] = (byte) (value >> 24);
            array[start + index + 5] = (byte) (value >> 24);
            array[start + index + 6] = (byte) (value >> 24);
            array[start + index + 7] = (byte) (value >> 24);
        }
        return this;
    }
/*
    @Override
    public LongBuffer asLongBuffer() {
        int sz = remaining() / 8;
        if (order == ByteOrder.BIG_ENDIAN) {
            return new TLongBufferOverByteBufferBigEndian(start + position, sz, this, 0, sz, isReadOnly());
        } else {
            return new TLongBufferOverByteBufferLittleEndian(start + position, sz, this, 0, sz, isReadOnly());
        }
    }

    @Override
    public FloatBuffer asFloatBuffer() {
        int sz = remaining() / 4;
        if (order == ByteOrder.LITTLE_ENDIAN) {
            return new TFloaBufferOverByteBufferBigEndian(start + position, sz, this, 0, sz, isReadOnly());
        } else {
            return new TFloaBufferOverByteBufferLittleEndian(start + position, sz, this, 0, sz, isReadOnly());
        }
    }

    @Override
    public DoubleBuffer asDoubleBuffer() {
        int sz = remaining() / 8;
        TDoubleBufferOverByteBuffer result = new TDoubleBufferOverByteBuffer(start + position, sz, this, 0, sz,
                isReadOnly());
        result.byteOrder = order;
        return result;
    }
    */
}
