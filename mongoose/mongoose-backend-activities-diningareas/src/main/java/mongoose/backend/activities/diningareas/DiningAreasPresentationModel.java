package mongoose.backend.activities.diningareas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class DiningAreasPresentationModel extends EventDependentGenericTablePresentationModel {

    private final ObjectProperty<DisplayResult> sittingDisplayResultProperty = new SimpleObjectProperty<>();
    public ObjectProperty<DisplayResult> sittingDisplayResultProperty() { return sittingDisplayResultProperty; }

    private final ObjectProperty<DisplayResult> rulesDisplayResultProperty = new SimpleObjectProperty<>();
    public ObjectProperty<DisplayResult> rulesDisplayResultProperty() { return rulesDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> rulesDisplaySelectionProperty = new SimpleObjectProperty<>();
    public ObjectProperty<DisplaySelection> rulesDisplaySelectionProperty() { return rulesDisplaySelectionProperty; }

}
