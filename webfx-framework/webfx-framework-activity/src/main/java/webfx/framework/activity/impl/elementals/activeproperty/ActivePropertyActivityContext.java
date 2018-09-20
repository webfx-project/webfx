package webfx.framework.activity.impl.elementals.activeproperty;

import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ActivePropertyActivityContext
        <THIS extends ActivePropertyActivityContext<THIS>>

        extends ActivityContext<THIS>,
        HasActiveProperty {

}
