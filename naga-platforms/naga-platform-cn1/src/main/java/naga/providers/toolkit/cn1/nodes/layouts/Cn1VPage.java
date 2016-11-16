package naga.providers.toolkit.cn1.nodes.layouts;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.cn1.nodes.Cn1Node;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;


/**
 * @author Bruno Salmon
 */
public class Cn1VPage extends Cn1Node<Container> implements VPage {

    public Cn1VPage() {
        super(new Container(new BorderLayout()));
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                ((Component) oldValue.unwrapToNativeNode()).remove();
            node.add(BorderLayout.NORTH, (Component) newValue.unwrapToNativeNode());
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                ((Component) oldValue.unwrapToNativeNode()).remove();
            node.add(BorderLayout.CENTER, (Component) newValue.unwrapToNativeNode());
        });
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                ((Component) oldValue.unwrapToNativeNode()).remove();
            node.add(BorderLayout.SOUTH, (Component) newValue.unwrapToNativeNode());
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
