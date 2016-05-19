package naga.core.spi.toolkit.nodes;


import javafx.beans.property.Property;
import naga.core.spi.toolkit.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface BorderPane<BP, N> extends GuiNode<BP> {

    Property<GuiNode<N>> topProperty();
    default BorderPane setTop(GuiNode node) { topProperty().setValue(node); return this;}
    default GuiNode<N> getTop() { return topProperty().getValue(); }

    Property<GuiNode<N>> centerProperty();
    default BorderPane setCenter(GuiNode node) { centerProperty().setValue(node); return this; }
    default GuiNode<N> getCenter() { return centerProperty().getValue(); }

    Property<GuiNode<N>> bottomProperty();
    default BorderPane setBottom(GuiNode node) { bottomProperty().setValue(node); return this; }
    default GuiNode<N> getBottom() { return bottomProperty().getValue(); }

}
