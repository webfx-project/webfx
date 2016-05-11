package naga.core.util.tuples;

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
}
