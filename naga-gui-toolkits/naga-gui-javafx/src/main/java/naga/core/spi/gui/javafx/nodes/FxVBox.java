package naga.core.spi.gui.javafx.nodes;


import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author Bruno Salmon
 */
public class FxVBox extends FxParent<VBox> implements naga.core.spi.gui.nodes.VBox<VBox, Node> {

    public FxVBox() {
        this(new VBox());
    }

    public FxVBox(VBox vbox) {
        super(vbox);
    }

}
