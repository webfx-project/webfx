package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;

public interface HasGenericDisplayResultProperty {

    ObjectProperty<DisplayResult> genericDisplayResultProperty();

    default DisplayResult getGenericDisplayResult() { return genericDisplayResultProperty().getValue(); }

    default void setGenericDisplayResult(DisplayResult value) { genericDisplayResultProperty().setValue(value); }

}
