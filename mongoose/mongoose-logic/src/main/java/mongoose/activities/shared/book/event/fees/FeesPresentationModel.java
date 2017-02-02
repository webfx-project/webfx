package mongoose.activities.shared.book.event.fees;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.activities.shared.book.event.shared.BookingProcessPresentationModel;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class FeesPresentationModel extends BookingProcessPresentationModel {

    // Display output

    private final Property<DisplayResultSet> dateInfoDisplayResultSetProperty = new SimpleObjectProperty<>();
    public Property<DisplayResultSet> dateInfoDisplayResultSetProperty() { return dateInfoDisplayResultSetProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onProgramAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onProgramActionProperty() { return onProgramAction; }
    public final void setOnProgramAction(EventHandler<ActionEvent> value) { onProgramActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnProgramAction() { return onProgramActionProperty().get(); }

    private final ObjectProperty<EventHandler<ActionEvent>> onTermsAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onTermsActionProperty() { return onTermsAction; }
    public final void setOnTermsAction(EventHandler<ActionEvent> value) { onTermsActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnTermsAction() { return onTermsActionProperty().get(); }
}
