package naga.commons.util.function;

/**
 * @author Bruno Salmon
 */

public interface BiConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);

}