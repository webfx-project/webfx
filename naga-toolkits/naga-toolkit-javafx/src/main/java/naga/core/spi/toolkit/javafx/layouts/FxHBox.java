package naga.core.spi.toolkit.javafx.layouts;


import javafx.scene.Node;
import javafx.scene.layout.HBox;
import naga.core.spi.toolkit.javafx.node.FxParent;

/**
 * @author Bruno Salmon
 */
public class FxHBox extends FxParent<HBox> implements naga.core.spi.toolkit.layouts.HBox<HBox, Node> {

    public FxHBox() {
        this(new HBox());
    }

    public FxHBox(HBox hbox) {
        super(hbox);
        //hbox.setAlignment(Pos.CENTER);
    }

}
