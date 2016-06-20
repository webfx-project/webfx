package mongoose.activities.organizations;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.ui.presentation.PresentationModel;

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

    // Display output

    private final Property<DisplayResultSet> organizationsDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> organizationDisplayResultSetProperty() { return organizationsDisplayResultSetProperty; }

}
