package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import mongoose.client.presentationmodel.HasSelectedGroupReferenceResolver;
import mongoose.client.presentationmodel.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.shared.entities.Document;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;

/**
 * @author Bruno Salmon
 */
final class BookingsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionEqlFilterStringProperty,
        HasGroupEqlFilterStringProperty,
        HasColumnsEqlFilterStringProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<Document>,
        HasSelectedGroupConditionEqlFilterStringProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterVisualResultProperty,
        HasMasterVisualSelectionProperty,
        HasSelectedMasterProperty<Document>,
        HasSelectedDocumentProperty {

    private final StringProperty conditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty conditionEqlFilterStringProperty() { return conditionEqlFilterStringProperty; }

    private final StringProperty groupEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty groupEqlFilterStringProperty() { return groupEqlFilterStringProperty; }

    private final StringProperty columnsEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty columnsEqlFilterStringProperty() { return columnsEqlFilterStringProperty; }

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<VisualSelection> groupVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> groupVisualSelectionProperty() { return groupVisualSelectionProperty; }

    private final ObjectProperty<Document> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Document> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionEqlFilterStringProperty() { return selectedGroupConditionEqlFilterStringProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<VisualResult> masterVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> masterVisualResultProperty() { return masterVisualResultProperty; }

    private final ObjectProperty<VisualSelection> masterVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> masterVisualSelectionProperty() { return masterVisualSelectionProperty; }

    private final ObjectProperty<Document> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Document> selectedMasterProperty() { return selectedMasterProperty; }

    @Override public ObjectProperty<Document> selectedDocumentProperty() { return selectedMasterProperty(); }

}
