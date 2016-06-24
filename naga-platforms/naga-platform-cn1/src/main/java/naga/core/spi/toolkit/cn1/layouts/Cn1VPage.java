package naga.core.spi.toolkit.cn1.layouts;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.cn1.node.Cn1Node;
import naga.core.spi.toolkit.layouts.VPage;


/**
 * @author Bruno Salmon
 */
public class Cn1VPage extends Cn1Node<Container> implements VPage<Container, Component> {

    public Cn1VPage() {
        super(new Container(new BorderLayout()));
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToNativeNode().remove();
            node.add(BorderLayout.NORTH, newValue.unwrapToNativeNode());
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToNativeNode().remove();
            node.add(BorderLayout.CENTER, newValue.unwrapToNativeNode());
        });
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToNativeNode().remove();
            node.add(BorderLayout.SOUTH, newValue.unwrapToNativeNode());
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
