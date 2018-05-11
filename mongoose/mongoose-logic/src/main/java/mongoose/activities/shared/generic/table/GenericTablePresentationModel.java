package mongoose.activities.shared.generic.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import naga.fxdata.displaydata.DisplayResult;
import naga.fxdata.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
public class GenericTablePresentationModel {

    // Display input

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    public Property<String> searchTextProperty() { return searchTextProperty; }

    private final IntegerProperty limitProperty = new SimpleIntegerProperty(0);
    public IntegerProperty limitProperty() { return limitProperty; }

    private final Property<DisplaySelection> genericDisplaySelectionProperty = new SimpleObjectProperty<>();
    public Property<DisplaySelection> genericDisplaySelectionProperty() { return genericDisplaySelectionProperty; }

    // Display output

    private final Property<DisplayResult> genericDisplayResultProperty = new SimpleObjectProperty<>();
    public Property<DisplayResult> genericDisplayResultProperty() { return genericDisplayResultProperty; }

}
