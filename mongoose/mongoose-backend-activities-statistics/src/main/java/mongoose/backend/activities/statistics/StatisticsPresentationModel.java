package mongoose.backend.activities.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mongoose.client.presentationmodel.HasSelectedGroupReferenceResolver;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.*;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;

/**
 * @author Bruno Salmon
 */
final class StatisticsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionEqlFilterStringProperty,
        HasGroupEqlFilterStringProperty,
        HasColumnsEqlFilterStringProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<DocumentLine>,
        HasSelectedGroupConditionEqlFilterStringProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterVisualResultProperty,
        HasMasterVisualSelectionProperty,
        HasSelectedMasterProperty<DocumentLine>,
        HasSelectedDocumentLineProperty,
        HasSelectedDocumentProperty {

    private final StringProperty conditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public final StringProperty conditionEqlFilterStringProperty() { return conditionEqlFilterStringProperty; }

    private final StringProperty groupEqlFilterStringProperty = new SimpleStringProperty();
    @Override public final StringProperty groupEqlFilterStringProperty() { return groupEqlFilterStringProperty; }

    private final StringProperty columnsEqlFilterStringProperty = new SimpleStringProperty();
    @Override public final StringProperty columnsEqlFilterStringProperty() { return columnsEqlFilterStringProperty; }

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<VisualSelection> groupVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> groupVisualSelectionProperty() { return groupVisualSelectionProperty; }

    private final ObjectProperty<DocumentLine> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DocumentLine> selectedGroupProperty() {
        return selectedGroupProperty;
    }

    private final StringProperty selectedGroupConditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionEqlFilterStringProperty() { return selectedGroupConditionEqlFilterStringProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<VisualResult> masterVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> masterVisualResultProperty() { return masterVisualResultProperty; }

    private final ObjectProperty<VisualSelection> masterVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> masterVisualSelectionProperty() { return masterVisualSelectionProperty; }

    @Override public ObjectProperty<DocumentLine> selectedMasterProperty() { return selectedDocumentLineProperty; }

    private final ObjectProperty<DocumentLine> selectedDocumentLineProperty = new SimpleObjectProperty<DocumentLine/*GWT*/>() {
        @Override
        protected void invalidated() {
            DocumentLine dl = get();
            setSelectedDocument(dl == null ? null : dl.getDocument());
        }
    };
    @Override public ObjectProperty<DocumentLine> selectedDocumentLineProperty() { return selectedDocumentLineProperty; }

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Document> selectedDocumentProperty() {
        return selectedDocumentProperty;
    }
}
