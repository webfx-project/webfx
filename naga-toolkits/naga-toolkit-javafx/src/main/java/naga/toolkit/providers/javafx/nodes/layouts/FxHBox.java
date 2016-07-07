package naga.toolkit.providers.javafx.nodes.layouts;


import javafx.scene.Node;
import javafx.scene.layout.HBox;
import naga.toolkit.providers.javafx.nodes.FxParent;

/**
 * @author Bruno Salmon
 */
public class FxHBox extends FxParent<HBox> implements naga.toolkit.spi.nodes.layouts.HBox<HBox, Node> {

    public FxHBox() {
        this(createHBox());
    }

    public FxHBox(HBox hbox) {
        super(hbox);
    }

    private static HBox createHBox() {
        return new HBox();
    }

}
