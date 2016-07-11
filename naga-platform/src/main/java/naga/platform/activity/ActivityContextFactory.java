package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public interface ActivityContextFactory<C extends ActivityContext> {

    C createContext(ActivityContext parentContext);

}
