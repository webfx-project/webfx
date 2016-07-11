package naga.platform.activity.client;

import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityContextImpl;

/**
 * @author Bruno Salmon
 */
public class ApplicationContextImpl<C extends ApplicationContextImpl<C>> extends ActivityContextImpl<C> implements ApplicationContext<C> {

    /**
     *  Global static instance of the application context that any activity can access if needed.
     *  In addition, this static instance is the root object of the application and it is necessary
     *  to keep a reference to it to avoid garbage collection.
     */
    static ApplicationContext instance;

    private String[] mainArgs;

    protected ApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(null, contextFactory);
        this.mainArgs = mainArgs;
        instance = this;
    }

    @Override
    public String[] getMainArgs() {
        return mainArgs;
    }
}
