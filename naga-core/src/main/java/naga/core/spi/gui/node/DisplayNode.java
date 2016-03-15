package naga.core.spi.gui.node;

import javafx.beans.property.Property;
import naga.core.ngui.displayresult.DisplayResult;

/**
 * @author Bruno Salmon
 */
public interface DisplayNode<N> extends Node<N> {

    Property<DisplayResult> displayResultProperty();
    default void setDisplayResult(DisplayResult selected) { displayResultProperty().setValue(selected); }
    default DisplayResult getDisplayResult() { return displayResultProperty().getValue(); }

}
