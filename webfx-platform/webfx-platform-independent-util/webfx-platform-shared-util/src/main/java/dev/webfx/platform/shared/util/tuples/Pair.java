package dev.webfx.platform.shared.util.tuples;

/**
 * @author Bruno Salmon
 */
public class Pair<T1, T2> {

    private T1 o1;
    private T2 o2;

    public Pair() {
    }

    public Pair(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public T1 get1() {
        return o1;
    }

    public void set1(T1 o1) {
        this.o1 = o1;
    }

    public T2 get2() {
        return o2;
    }

    public void set2(T2 o2) {
        this.o2 = o2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (o1 != null ? !o1.equals(pair.o1) : pair.o1 != null) return false;
        return o2 != null ? o2.equals(pair.o2) : pair.o2 == null;
    }

    @Override
    public int hashCode() {
        int result = o1 != null ? o1.hashCode() : 0;
        result = 31 * result + (o2 != null ? o2.hashCode() : 0);
        return result;
    }
}
