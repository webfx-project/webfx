package mongoose.logic.organizations;

import javafx.beans.property.*;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.ngui.displayselection.DisplaySelection;
import naga.core.ngui.presentation.PresentationModel;
import naga.core.spi.toolkit.event.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationModel implements PresentationModel {

    // Display input

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> limitProperty() { return limitProperty; }

    private final Property<DisplaySelection> organizationsDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> organizationsDisplaySelectionProperty() { return organizationsDisplaySelectionProperty; }

    private final BehaviorSubject<ActionEvent> testButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> testButtonActionEventObservable() { return testButtonActionEventObservable; }

    // Display output

    private final Property<DisplayResultSet> organizationsDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> organizationDisplayResultSetProperty() { return organizationsDisplayResultSetProperty; }

}
