package naga.framework.activity.base.elementals.activeproperty;

import naga.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ActivePropertyActivityContext
        <THIS extends ActivePropertyActivityContext<THIS>>

        extends ActivityContext<THIS>,
        HasActiveProperty {

}
