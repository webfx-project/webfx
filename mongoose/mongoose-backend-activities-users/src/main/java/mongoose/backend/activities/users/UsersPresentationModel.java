package mongoose.backend.activities.users;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.*;
import mongoose.shared.entities.Person;
import webfx.framework.shared.expression.builder.ReferenceResolver;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class UsersPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasColumnsStringFilterProperty,
        HasGroupDisplayResultProperty,
        HasGroupDisplaySelectionProperty,
        HasSelectedGroupProperty<Person>,
        HasSelectedGroupConditionStringFilterProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterDisplayResultProperty,
        HasMasterDisplaySelectionProperty,
        HasSelectedMasterProperty<Person>{

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

    private final ObjectProperty<Person> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Person> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<DisplayResult> masterDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> masterDisplayResultProperty() { return masterDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> masterDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> masterDisplaySelectionProperty() { return masterDisplaySelectionProperty; }

    private final ObjectProperty<Person> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<Person> selectedMasterProperty() { return selectedMasterProperty; }
}
