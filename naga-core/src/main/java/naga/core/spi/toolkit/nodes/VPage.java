package naga.core.spi.toolkit.nodes;


import javafx.beans.property.Property;
import naga.core.spi.toolkit.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface VPage<BP, N> extends GuiNode<BP> {

    Property<GuiNode<N>> headerProperty();
    default VPage setHeader(GuiNode node) { headerProperty().setValue(node); return this;}
    default GuiNode<N> getHeader() { return headerProperty().getValue(); }

    Property<GuiNode<N>> centerProperty();
    default VPage setCenter(GuiNode node) { centerProperty().setValue(node); return this; }
    default GuiNode<N> getCenter() { return centerProperty().getValue(); }

    Property<GuiNode<N>> footerProperty();
    default VPage setFooter(GuiNode node) { footerProperty().setValue(node); return this; }
    default GuiNode<N> getFooter() { return footerProperty().getValue(); }

}
