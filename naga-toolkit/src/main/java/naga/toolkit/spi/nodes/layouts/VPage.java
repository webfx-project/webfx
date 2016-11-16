package naga.toolkit.spi.nodes.layouts;


import javafx.beans.property.Property;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface VPage extends GuiNode {

    Property<GuiNode> headerProperty();
    default VPage setHeader(GuiNode node) { headerProperty().setValue(node); return this;}
    default GuiNode getHeader() { return headerProperty().getValue(); }

    Property<GuiNode> centerProperty();
    default VPage setCenter(GuiNode node) { centerProperty().setValue(node); return this; }
    default GuiNode getCenter() { return centerProperty().getValue(); }

    Property<GuiNode> footerProperty();
    default VPage setFooter(GuiNode node) { footerProperty().setValue(node); return this; }
    default GuiNode getFooter() { return footerProperty().getValue(); }

}
