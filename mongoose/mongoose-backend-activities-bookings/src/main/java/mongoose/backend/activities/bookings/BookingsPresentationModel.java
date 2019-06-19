package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import mongoose.backend.controls.masterslave.group.HasGroupDisplayResultProperty;
import mongoose.backend.controls.masterslave.group.HasGroupDisplaySelectionProperty;
import mongoose.backend.controls.masterslave.group.HasSelectedGroupConditionStringFilterProperty;
import mongoose.backend.controls.masterslave.group.HasSelectedGroupProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.entities.util.filters.HasColumnsStringFilterProperty;
import mongoose.client.entities.util.filters.HasConditionStringFilterProperty;
import mongoose.client.entities.util.filters.HasGroupStringFilterProperty;
import mongoose.shared.entities.Document;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class BookingsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasColumnsStringFilterProperty,
        HasGroupDisplayResultProperty,
        HasGroupDisplaySelectionProperty,
        HasSelectedGroupConditionStringFilterProperty,
        HasSelectedGroupProperty<Document> {

    private final ObjectProperty<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final StringProperty columnsStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty columnsStringFilterProperty() { return columnsStringFilterProperty; }

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();
    ObjectProperty<Document> selectedDocumentProperty() {
        return selectedDocumentProperty;
    }
    void setSelectedDocument(Document document) {
        selectedDocumentProperty.set(document);
    }

    private final ObjectProperty<Document> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Document> selectedGroupProperty() {
        return selectedGroupProperty;
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

}
