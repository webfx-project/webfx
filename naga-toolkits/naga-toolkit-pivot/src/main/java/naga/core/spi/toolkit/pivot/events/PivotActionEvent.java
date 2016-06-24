package naga.core.spi.toolkit.pivot.events;

import naga.core.spi.toolkit.events.ActionEvent;
import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
public class PivotActionEvent implements ActionEvent {

    private final Component source;

    public PivotActionEvent(Component source) {
        this.source = source;
    }
}
