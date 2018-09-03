package mongooses.core.activities.backend.loadtester.saveloadtest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Jean-Pierre Alonso.
 */
class SaveLoadTestPresentationModel {

    private final Property<String> testNameProperty = new SimpleObjectProperty<>();
    Property<String> testNameProperty() { return testNameProperty; }

    private final Property<String> testCommentProperty = new SimpleObjectProperty<>();
    Property<String> testCommentProperty() { return testCommentProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onSaveTest = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onSaveTestProperty() { return onSaveTest; }
    public final void setOnSaveTest(EventHandler<ActionEvent> value) { onSaveTestProperty().set(value); }
    public final EventHandler<ActionEvent> getOnSaveTest() { return onSaveTestProperty().get(); }

}
