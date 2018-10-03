package webfx.framework.client.activity.impl.elementals.application.impl;

import webfx.framework.client.activity.impl.ActivityContextBase;
import webfx.framework.client.activity.ActivityContextFactory;
import webfx.framework.client.activity.impl.elementals.application.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public class ApplicationContextBase
        <THIS extends ApplicationContextBase<THIS>>

        extends ActivityContextBase<THIS>
        implements ApplicationContext<THIS> {

    /**
     *  Global static instance of the application context that any activity can access if needed.
     *  In addition, this static instance is the root object of the application and it is necessary
     *  to keep a reference to it to avoid garbage collection.
     */
    public static ApplicationContext instance;

    public static String[] mainArgs;

    protected ApplicationContextBase(String[] mainArgs, ActivityContextFactory<THIS> contextFactory) {
        super(null, contextFactory);
        registerRootFields(this, mainArgs);
    }

    public static void registerRootFields(ApplicationContext rootInstance, String[] rootMainArgs) {
        instance = rootInstance;
        mainArgs = rootMainArgs;
    }
}
