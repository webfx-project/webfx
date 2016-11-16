package naga.providers.toolkit.javafx.nodes.layouts;


import javafx.scene.layout.HBox;
import naga.providers.toolkit.javafx.nodes.FxParent;

/**
 * @author Bruno Salmon
 */
public class FxHBox extends FxParent<HBox> implements naga.toolkit.spi.nodes.layouts.HBox {

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
