package naga.platform.activity.client;

import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContext<C extends ApplicationContext<C>> extends ActivityContext<C> {

    static <C extends ApplicationContext<C>> C get() {
        return (C) ApplicationContextImpl.instance;
    }

    String[] getMainArgs();

    static ApplicationContext create(String[] mainArgs) {
        return new ApplicationContextImpl(mainArgs, ActivityContext::create);
    }
}
