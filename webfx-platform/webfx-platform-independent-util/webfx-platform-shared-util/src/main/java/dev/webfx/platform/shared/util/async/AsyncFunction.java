package dev.webfx.platform.shared.util.async;

import java.util.function.Function;

/**
 * A functional interface for asynchronous operations that take one argument and return a future result.
 *
 * @author Bruno Salmon
 */

public interface AsyncFunction<A,R> extends Function<A, Future<R>> {

    Future<R> apply(A arg);

}
