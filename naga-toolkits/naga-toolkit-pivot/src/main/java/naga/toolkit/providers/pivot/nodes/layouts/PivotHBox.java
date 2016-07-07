package naga.toolkit.providers.pivot.nodes.layouts;

import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.providers.pivot.nodes.PivotParent;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotHBox extends PivotParent<BoxPane> implements HBox<BoxPane, Component> {

    public PivotHBox() {
        super(new BoxPane());
        node.setOrientation(Orientation.HORIZONTAL);
        node.getStyles().put("fill", true);
    }

}
