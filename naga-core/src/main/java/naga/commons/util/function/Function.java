package naga.commons.util.function;

/**
 * @author Bruno Salmon
 */

public interface Function<A, R> extends ThrowableFunction<A, R> {

    @Override
    R apply(A arg);

}
