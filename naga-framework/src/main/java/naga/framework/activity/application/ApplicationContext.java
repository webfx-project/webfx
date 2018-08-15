package naga.framework.activity.application;

import naga.framework.activity.ActivityContext;
import naga.framework.activity.application.impl.ApplicationContextBase;
import naga.framework.activity.application.impl.ApplicationContextFinal;

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
