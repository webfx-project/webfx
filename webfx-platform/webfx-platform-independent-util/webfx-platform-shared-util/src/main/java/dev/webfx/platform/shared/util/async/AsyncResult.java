package dev.webfx.platform.shared.util.async;

/**
 * Represents the result of an asynchronous operation. Normally AsyncResult is returned only once the asynchronous
 * operation is finished and therefore it represents its final result (whatever it has succeeded or failed).
 *
 * However when it is an instance of Future (a subclass), it is returned immediately when invoking the asynchronous
 * operation (see AsyncFunction for example) so in this case the result is not yet known until the operation is finished
 * and the result handler called (see Future.setHandler).
 *
 * @author Bruno Salmon
 */

public interface AsyncResult<T> {
    /**
     * The result of the operation. This will be null if the operation failed.
     */
    T result();

    /**
     * An exception describing failure. This will be null if the operation succeeded.
     */
    Throwable cause();

    /**
     * Did it succeed?
     *
     * @return true if it succeeded or false otherwise
     */
    boolean succeeded();

    /**
     * Did it fail?
     *
     * @return true if it failed or false otherwise
     */
    boolean failed();
}