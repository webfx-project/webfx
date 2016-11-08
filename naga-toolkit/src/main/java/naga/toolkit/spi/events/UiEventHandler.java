package naga.toolkit.spi.events;

import naga.commons.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface UiEventHandler<E extends UiEvent> extends Handler<E> {
}
