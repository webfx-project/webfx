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
import webfx.framework.shared.expression.builder.ReferenceResolver;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class StatisticsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasColumnsStringFilterProperty,
        HasGroupDisplayResultProperty,
        HasGroupDisplaySelectionProperty,
        HasSelectedGroupProperty<DocumentLine>,
        HasSelectedGroupConditionStringFilterProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterDisplayResultProperty,
        HasMasterDisplaySelectionProperty,
        HasSelectedMasterProperty<DocumentLine>,
        HasSelectedDocumentLineProperty,
        HasSelectedDocumentProperty {

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    @Override public final StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override public final StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final StringProperty columnsStringFilterProperty = new SimpleStringProperty();
    @Override public final StringProperty columnsStringFilterProperty() { return columnsStringFilterProperty; }

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final ObjectProperty<DocumentLine> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DocumentLine> selectedGroupProperty() {
        return selectedGroupProperty;
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<DisplayResult> masterDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> masterDisplayResultProperty() { return masterDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> masterDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> masterDisplaySelectionProperty() { return masterDisplaySelectionProperty; }

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
