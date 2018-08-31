package webfx.util.function;

/**
 * @author Bruno Salmon
 */

public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
