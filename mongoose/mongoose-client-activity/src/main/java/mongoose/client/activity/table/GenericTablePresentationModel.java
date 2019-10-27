package mongoose.client.activity.table;

import javafx.beans.property.*;
import mongoose.client.presentationmodel.HasGenericVisualResultProperty;
import mongoose.client.presentationmodel.HasGenericVisualSelectionProperty;
import mongoose.client.presentationmodel.HasLimitProperty;
import mongoose.client.presentationmodel.HasSearchTextProperty;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;

/**
 * @author Bruno Salmon
 */
public class GenericTablePresentationModel implements
        HasSearchTextProperty,
        HasLimitProperty,
        HasGenericVisualResultProperty,
        HasGenericVisualSelectionProperty {

    // Display input

    private final StringProperty searchTextProperty = new SimpleStringProperty();
    @Override  public StringProperty searchTextProperty() { return searchTextProperty; }

    private final IntegerProperty limitProperty = new SimpleIntegerProperty(0);
    @Override public IntegerProperty limitProperty() { return limitProperty; }

    private final ObjectProperty<VisualSelection> genericVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> genericVisualSelectionProperty() { return genericVisualSelectionProperty; }

    // Display output

    private final ObjectProperty<VisualResult> genericVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> genericVisualResultProperty() { return genericVisualResultProperty; }

}
