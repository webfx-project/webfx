package naga.providers.toolkit.javafx.events;

import naga.toolkit.spi.events.MouseEvent;

/**
 * @author Bruno Salmon
 */
public class FxMouseEvent extends FxEvent<javafx.scene.input.MouseEvent> implements MouseEvent {

    public FxMouseEvent(javafx.scene.input.MouseEvent fxEvent) {
        super(fxEvent);
    }
}
