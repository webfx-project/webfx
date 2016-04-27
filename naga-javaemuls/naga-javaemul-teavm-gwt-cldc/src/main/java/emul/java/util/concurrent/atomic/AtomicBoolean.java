package emul.java.util.concurrent.atomic;

public class AtomicBoolean implements java.io.Serializable {
    private boolean value;

    public AtomicBoolean(boolean initialValue) {
        value = initialValue;
    }

    public AtomicBoolean() {
    }

    public final boolean get() {
        return value;
    }

    public final boolean compareAndSet(boolean expect, boolean update) {
        if (value != expect) return false;
        value = update;
        return true;
    }

    public boolean weakCompareAndSet(boolean expect, boolean update) {
        return compareAndSet(expect, update);
    }

    public final void set(boolean newValue) {
        value = newValue;
    }

    public final void lazySet(boolean newValue) {
        set(newValue);
    }

    public final boolean getAndSet(boolean newValue) {
        boolean prev = get();
        value = newValue;
        return prev;
    }

    public String toString() {
        return get() ? "true" : "false";
    }

}
