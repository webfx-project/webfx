package naga.framework.activity.activeproperty;

import javafx.beans.property.ReadOnlyProperty;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ActivePropertyActivityContext
        <THIS extends ActivePropertyActivityContext<THIS>>

        extends ActivityContext<THIS> {

    ReadOnlyProperty<Boolean> activeProperty();

}
