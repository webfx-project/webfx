package emul.java.lang;

/**
 * Minimalist ThreadLocal GWT implementation that works in the browser (javascript => single thread environment)
 *
 * @author Bruno Salmon
 */

public class ThreadLocal<T> {

    private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void remove() {
        value = null;
    }
}