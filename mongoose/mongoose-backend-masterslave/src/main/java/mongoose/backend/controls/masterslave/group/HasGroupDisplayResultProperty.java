package mongoose.backend.controls.masterslave.group;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;

public interface HasGroupDisplayResultProperty {

    ObjectProperty<DisplayResult> groupDisplayResultProperty();
    default DisplayResult getGroupDisplayResult() { return groupDisplayResultProperty().get();}
    default void setGroupDisplayResult(DisplayResult value) { groupDisplayResultProperty().set(value);}

}
