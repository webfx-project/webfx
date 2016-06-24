package naga.core.spi.toolkit.pivot.layouts;

import naga.core.spi.toolkit.layouts.VBox;
import naga.core.spi.toolkit.pivot.node.PivotParent;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotVBox extends PivotParent<BoxPane> implements VBox<BoxPane, Component> {

    public PivotVBox() {
        super(new BoxPane());
        node.setOrientation(Orientation.VERTICAL);
        node.getStyles().put("fill", true);
    }

}
