package mongoose.activities.tester.testset;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;

/**
 * @author Jean-Pierre Alonso.
 */
class TestSetPresentationModel  implements PresentationModel {

    private final Property<String> testNameProperty = new SimpleObjectProperty<>();
    Property<String> testNameProperty() { return testNameProperty; }

    private final Property<String> testCommentProperty = new SimpleObjectProperty<>();
    Property<String> testCommentProperty() { return testCommentProperty; }

}
