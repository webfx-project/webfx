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

/**
 *
 * @author Alexey Andreev
 */
public abstract class Buffer {
    int capacity;
    int position;
    int limit;
    int mark = -1;

    Buffer(int capacity) {
        this.capacity = capacity;
        limit = capacity;
    }

    public final int capacity() {
        return capacity;
    }

    public final int position() {
        return position;
    }

    public final Buffer position(int newPosition) {
        if (newPosition < 0 || newPosition > limit) {
            throw new IllegalArgumentException("New position " + newPosition + " is outside of range [0;"
                    + limit + "]");
        }
        position = newPosition;
        if (newPosition < mark) {
            mark = 0;
        }
        return this;
    }

    public final int limit() {
        return limit;
    }

    public final Buffer limit(int newLimit) {
        if (newLimit < 0 || newLimit > capacity) {
            throw new IllegalArgumentException("New limit " + newLimit + " is outside of range [0;"
                    + capacity + "]");
        }
        if (mark > newLimit) {
            mark = -1;
        }
        limit = newLimit;
        if (position > limit) {
            position = limit;
        }
        return this;
    }

    public final Buffer mark() {
        mark = position;
        return this;
    }

    public final Buffer reset() {
        if (mark < 0) {
            throw new InvalidMarkException();
        }
        position = mark;
        return this;
    }

    public final Buffer clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return this;
    }

    public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }

    public final Buffer rewind() {
        mark = -1;
        position = 0;
        return this;
    }

    public final int remaining() {
        return limit - position;
    }

    public final boolean hasRemaining() {
        return position < limit;
    }

    public abstract boolean isReadOnly();

    public abstract boolean hasArray();

    public abstract Object array();

    public abstract int arrayOffset();

    public abstract boolean isDirect();
}
