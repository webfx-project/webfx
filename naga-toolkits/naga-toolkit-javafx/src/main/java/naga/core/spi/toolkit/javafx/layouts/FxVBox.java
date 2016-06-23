package naga.core.spi.toolkit.javafx.layouts;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import naga.core.spi.toolkit.javafx.node.FxParent;

/**
 * @author Bruno Salmon
 */
public class FxVBox extends FxParent<VBox> implements naga.core.spi.toolkit.layouts.VBox<VBox, Node> {

    public FxVBox() {
        this(new VBox());
    }

    public FxVBox(VBox vbox) {
        super(vbox);
        vbox.setAlignment(Pos.CENTER);
    }

}
