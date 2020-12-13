package dev.webfx.platform.shared.util.tuples;

/**
 * @author Bruno Salmon
 */
public final class Triplet<T1, T2, T3> extends Pair<T1, T2> {

    private T3 o3;

    public Triplet() {
    }

    public Triplet(T1 o1, T2 o2, T3 o3) {
        super(o1, o2);
        this.o3 = o3;
    }

    public T3 get3() {
        return o3;
    }

    public void set3(T3 o3) {
        this.o3 = o3;
    }
}
