package naga.core.util.function;

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
