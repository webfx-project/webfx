package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import mongoose.client.presentationmodel.HasSelectedGroupReferenceResolver;
import mongoose.client.presentationmodel.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.shared.entities.Document;
import webfx.framework.shared.expression.builder.ReferenceResolver;
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
        HasSelectedGroupProperty<Document>,
        HasSelectedGroupConditionStringFilterProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterDisplayResultProperty,
        HasMasterDisplaySelectionProperty,
        HasSelectedMasterProperty<Document>,
        HasSelectedDocumentProperty {

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final StringProperty columnsStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty columnsStringFilterProperty() { return columnsStringFilterProperty; }

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final ObjectProperty<Document> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Document> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<DisplayResult> masterDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> masterDisplayResultProperty() { return masterDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> masterDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> masterDisplaySelectionProperty() { return masterDisplaySelectionProperty; }

    private final ObjectProperty<Document> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Document> selectedMasterProperty() { return selectedMasterProperty; }

    @Override public ObjectProperty<Document> selectedDocumentProperty() { return selectedMasterProperty(); }

}
