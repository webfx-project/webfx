package mongoose.backend.activities.statistics;

import javafx.beans.property.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.entities.util.filters.HasColumnsStringFilterProperty;
import mongoose.client.entities.util.filters.HasConditionStringFilterProperty;
import mongoose.client.entities.util.filters.HasGroupStringFilterProperty;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class StatisticsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasColumnsStringFilterProperty {

    private final Property<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    public final StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    public final StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final StringProperty columnsStringFilterProperty = new SimpleStringProperty();
    public final StringProperty columnsStringFilterProperty() { return columnsStringFilterProperty; }

    private final ObjectProperty<DocumentLine> selectedDocumentLineProperty = new SimpleObjectProperty<>();
    ObjectProperty<DocumentLine> selectedDocumentLineProperty() {
        return selectedDocumentLineProperty;
    }
    void setSelectedDocumentLine(DocumentLine document) {
        selectedDocumentLineProperty.set(document);
    }

    private final ObjectProperty<DocumentLine> selectedGroupProperty = new SimpleObjectProperty<>();
    ObjectProperty<DocumentLine> selectedGroupProperty() {
        return selectedGroupProperty;
    }
    void setSelectedGroup(DocumentLine selectedGroup) {
        selectedGroupProperty.set(selectedGroup);
    }
    DocumentLine getSelectedGroup() {
        return selectedGroupProperty.get();
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();
    ObjectProperty<Document> selectedDocumentProperty() {
        return selectedDocumentProperty;
    }
    void setSelectedDocument(Document document) {
        selectedDocumentProperty.set(document);
    }

}
