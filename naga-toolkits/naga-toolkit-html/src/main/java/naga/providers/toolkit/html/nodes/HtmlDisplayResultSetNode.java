package naga.providers.toolkit.html.nodes;

import elemental2.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlDisplayResultSetNode<N extends Node> extends HtmlNode<N> implements DisplayResultSetNode<N> {

    public HtmlDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> {
            try {
                syncVisualDisplayResult(newValue);
            } catch (Throwable e) {
                System.out.println("Exception while calling GwtDisplayResultSetNode.syncVisualDisplayResult(): " + e.getClass());
                e.printStackTrace();
            }
        });
    }

    private final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void syncVisualDisplayResult(DisplayResultSet rs);
}
