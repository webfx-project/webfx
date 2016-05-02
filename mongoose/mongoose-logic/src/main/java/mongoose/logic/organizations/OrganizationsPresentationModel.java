package mongoose.logic.organizations;

import javafx.beans.property.*;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.ngui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationModel implements PresentationModel {

    // User inputs

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> limitProperty() { return limitProperty; }

    // Display output

    private final Property<DisplayResultSet> organizationDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> organizationDisplayResultSetProperty() { return organizationDisplayResultSetProperty; }

}
