package webfx.framework.activity.base.elementals.application;

import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.base.elementals.application.impl.ApplicationContextBase;
import webfx.framework.activity.base.elementals.application.impl.ApplicationContextFinal;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContext
        <THIS extends ApplicationContext<THIS>>

        extends ActivityContext<THIS> {

    static <C extends ApplicationContext<C>> C get() {
        return (C) ApplicationContextBase.instance;
    }

    default String[] getMainArgs() {
        return ApplicationContextBase.mainArgs;
    }

    static ApplicationContextFinal create(String[] mainArgs) {
        return new ApplicationContextFinal(mainArgs, ActivityContext::create);
    }
}
