package naga.toolkit.fx.properties.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPromptTextProperty {

    Property<String> promptTextProperty();
    default void setPromptText(String promptText) { promptTextProperty().setValue(promptText); }
    default String getPromptText() { return promptTextProperty().getValue(); }

}
