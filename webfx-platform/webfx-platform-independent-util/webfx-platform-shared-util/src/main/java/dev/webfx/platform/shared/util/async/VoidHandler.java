package dev.webfx.platform.shared.util.async;

/**
 * This class can be used for simple handlers which don't receive any value.
 *
 * @author Bruno Salmon
 */

public abstract class VoidHandler implements Handler<Void> {

    @Override
    public final void handle(Void event) {
        handle();
    }

    /**
     * Handle the event. It should be overridden by the user.
     */
    protected abstract void handle();
}
