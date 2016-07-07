package naga.toolkit.providers.javafx.nodes.layouts;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import naga.toolkit.providers.javafx.nodes.FxParent;

/**
 * @author Bruno Salmon
 */
public class FxVBox extends FxParent<VBox> implements naga.toolkit.spi.nodes.layouts.VBox<VBox, Node> {

    public FxVBox() {
        this(createVBox());
    }

    public FxVBox(VBox vbox) {
        super(vbox);
    }

    private static VBox createVBox() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

}
