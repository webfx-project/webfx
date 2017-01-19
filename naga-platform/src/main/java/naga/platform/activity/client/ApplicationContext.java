package naga.platform.activity.client;

import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContext<C extends ApplicationContext<C>> extends ActivityContext<C> {

    static <C extends ApplicationContext<C>> C get() {
        return (C) ApplicationContextExtendable.instance;
    }

    String[] getMainArgs();

    static ApplicationContextFinal create(String[] mainArgs) {
        return new ApplicationContextFinal(mainArgs, ActivityContext::create);
    }
}
