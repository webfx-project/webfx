package naga.core.spi.toolkit.pivot.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.pivot.node.PivotNode;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotVPage extends PivotNode<BoxPane> implements VPage<BoxPane, Component> {

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


    private final Property<GuiNode<Component>> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode<Component>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<Component>> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> footerProperty() {
        return footerProperty;
    }
}
