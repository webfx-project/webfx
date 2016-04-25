package mongoose.logic.organizations;

import javafx.beans.property.*;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.ngui.presentationmodel.PresentationModel;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationModel implements PresentationModel {

    // User inputs

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    public Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    public Property<Boolean> limitProperty() { return limitProperty; }

    // Display output

    private final Property<DisplayResult> organizationDisplayResultProperty = new SimpleObjectProperty<>();
    public Property<DisplayResult> organizationDisplayResultProperty() { return organizationDisplayResultProperty; }

}
