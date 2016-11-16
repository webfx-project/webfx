package naga.providers.toolkit.pivot.nodes.layouts;

import naga.providers.toolkit.pivot.nodes.PivotParent;
import naga.toolkit.spi.nodes.layouts.HBox;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotHBox extends PivotParent<BoxPane> implements HBox {

    public PivotHBox() {
        super(new BoxPane());
        node.setOrientation(Orientation.HORIZONTAL);
        node.getStyles().put("fill", true);
    }

}
