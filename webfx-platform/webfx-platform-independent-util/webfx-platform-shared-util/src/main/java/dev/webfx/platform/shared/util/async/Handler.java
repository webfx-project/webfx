package dev.webfx.platform.shared.util.async;

/**
 * @author Bruno Salmon
 */

public interface Handler<E> {

    /**
     * Something has happened, so handle it.
     */
    void handle(E event);
}
