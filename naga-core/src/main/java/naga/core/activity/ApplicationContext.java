package naga.core.activity;

/**
 * @author Bruno Salmon
 */
public class ApplicationContext extends ActivityContext {

    /**
     *  Global static instance of the application context that any activity can access if needed.
     *  In addition, this static instance is the root object of the application and it is necessary
     *  to keep a reference to it to avoid garbage collection.
     */
    private static ApplicationContext instance;
    public static ApplicationContext get() {
        return instance;
    }

    private String[] mainArgs;

    ApplicationContext(String[] mainArgs) {
        this.mainArgs = mainArgs;
        instance = this;
    }

    public String[] getMainArgs() {
        return mainArgs;
    }
}
