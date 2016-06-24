package naga.core.spi.toolkit.pivot.layouts;

import naga.core.spi.toolkit.layouts.HBox;
import naga.core.spi.toolkit.pivot.node.PivotParent;
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
