package mongoose.backend.activities.users;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.*;
import mongoose.shared.entities.Person;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;

/**
 * @author Bruno Salmon
 */
final class UsersPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionEqlFilterStringProperty,
        HasGroupEqlFilterStringProperty,
        HasColumnsEqlFilterStringProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<Person>,
        HasSelectedGroupConditionEqlFilterStringProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterVisualResultProperty,
        HasMasterVisualSelectionProperty,
        HasSelectedMasterProperty<Person>{

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

    private final ObjectProperty<Person> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Person> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionEqlFilterStringProperty() { return selectedGroupConditionEqlFilterStringProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<VisualResult> masterVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> masterVisualResultProperty() { return masterVisualResultProperty; }

    private final ObjectProperty<VisualSelection> masterVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> masterVisualSelectionProperty() { return masterVisualSelectionProperty; }

    private final ObjectProperty<Person> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Person> selectedMasterProperty() { return selectedMasterProperty; }
}
