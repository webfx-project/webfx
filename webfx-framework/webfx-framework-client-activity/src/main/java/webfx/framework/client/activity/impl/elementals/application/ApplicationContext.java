package webfx.framework.client.activity.impl.elementals.application;

import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.impl.elementals.application.impl.ApplicationContextBase;
import webfx.framework.client.activity.impl.elementals.application.impl.ApplicationContextFinal;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContext
        <THIS extends ApplicationContext<THIS>>

        extends ActivityContext<THIS> {

    static <C extends ApplicationContext<C>> C get() {
        return (C) ApplicationContextBase.instance;
    }

    static ApplicationContextFinal create() {
        return new ApplicationContextFinal(ActivityContext::create);
    }
}
