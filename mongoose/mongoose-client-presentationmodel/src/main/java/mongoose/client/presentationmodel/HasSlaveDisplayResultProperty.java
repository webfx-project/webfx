package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;

public interface HasSlaveDisplayResultProperty {

    ObjectProperty<DisplayResult> slaveDisplayResultProperty();

    default DisplayResult getSlaveDisplayResult() { return slaveDisplayResultProperty().getValue(); }

    default void setSlaveDisplayResult(DisplayResult value) { slaveDisplayResultProperty().setValue(value); }

}
