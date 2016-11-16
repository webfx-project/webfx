package naga.providers.toolkit.pivot.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.pivot.nodes.PivotNode;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotVPage extends PivotNode<BoxPane> implements VPage {

    public PivotVPage() {
        super(new BoxPane());
        node.setOrientation(Orientation.VERTICAL);
        node.getStyles().put("fill", true);
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
    }


    private final Property<GuiNode> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> footerProperty() {
        return footerProperty;
    }
}
