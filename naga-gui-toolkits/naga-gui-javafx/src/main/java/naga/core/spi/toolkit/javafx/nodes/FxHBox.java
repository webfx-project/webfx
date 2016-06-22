package naga.core.spi.toolkit.javafx.nodes;


import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public class FxHBox extends FxParent<HBox> implements naga.core.spi.toolkit.nodes.HBox<HBox, Node> {

    public FxHBox() {
        this(new HBox());
    }

    public FxHBox(HBox hbox) {
        super(hbox);
        //hbox.setAlignment(Pos.CENTER);
    }

}
