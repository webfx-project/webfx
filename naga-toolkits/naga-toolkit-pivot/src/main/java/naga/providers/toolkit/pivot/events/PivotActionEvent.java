package naga.providers.toolkit.pivot.events;

import naga.toolkit.spi.events.ActionEvent;
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
