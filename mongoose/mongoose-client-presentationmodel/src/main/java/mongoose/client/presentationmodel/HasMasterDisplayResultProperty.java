package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;

public interface HasMasterDisplayResultProperty {

    ObjectProperty<DisplayResult> masterDisplayResultProperty();

    default DisplayResult getMasterDisplayResult() { return masterDisplayResultProperty().getValue(); }

    default void setMasterDisplayResult(DisplayResult value) { masterDisplayResultProperty().setValue(value); }

}
