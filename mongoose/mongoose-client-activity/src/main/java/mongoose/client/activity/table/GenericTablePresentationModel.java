package mongoose.client.activity.table;

import javafx.beans.property.*;
import mongoose.client.presentationmodel.HasGenericDisplayResultProperty;
import mongoose.client.presentationmodel.HasGenericDisplaySelectionProperty;
import mongoose.client.presentationmodel.HasLimitProperty;
import mongoose.client.presentationmodel.HasSearchTextProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
public class GenericTablePresentationModel implements
        HasSearchTextProperty,
        HasLimitProperty,
        HasGenericDisplayResultProperty,
        HasGenericDisplaySelectionProperty {

    // Display input

    private final StringProperty searchTextProperty = new SimpleStringProperty();
    @Override  public StringProperty searchTextProperty() { return searchTextProperty; }

    private final IntegerProperty limitProperty = new SimpleIntegerProperty(0);
    @Override public IntegerProperty limitProperty() { return limitProperty; }

    private final ObjectProperty<DisplaySelection> genericDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> genericDisplaySelectionProperty() { return genericDisplaySelectionProperty; }

    // Display output

    private final ObjectProperty<DisplayResult> genericDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> genericDisplayResultProperty() { return genericDisplayResultProperty; }

}
