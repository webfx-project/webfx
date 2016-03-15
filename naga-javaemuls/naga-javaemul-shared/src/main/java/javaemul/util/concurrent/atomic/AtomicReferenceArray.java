package javaemul.util.concurrent.atomic;

import java.util.Arrays;

public class AtomicReferenceArray<E> implements java.io.Serializable {

    private final Object[] array; // must have exact type Object[]

    public AtomicReferenceArray(int length) {
        array = new Object[length];
    }

    public AtomicReferenceArray(E[] array) {
        this.array = Arrays.copyOf(array, array.length);
    }

    public final int length() {
        return array.length;
    }

    public final E get(int i) {
        return (E) array[i];
    }

    public final void set(int i, E newValue) {
        array[i] = newValue;
    }

    public final void lazySet(int i, E newValue) {
        set(i, newValue);
    }

    public final E getAndSet(int i, E newValue) {
        final Object prev = array[i];
        array[i] = newValue;
        return (E) prev;
    }

    public final boolean compareAndSet(int i, E expect, E update) {
        if (array[i] == expect) {
            array[i] = update;
            return true;
        } else {
            return false;
        }
    }

    public final boolean weakCompareAndSet(int i, E expect, E update) {
        return compareAndSet(i, expect, update);
    }

    public String toString() {
        return Arrays.toString(array);
    }
}
