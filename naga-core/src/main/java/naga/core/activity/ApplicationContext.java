package naga.core.activity;

/**
 * @author Bruno Salmon
 */
public class ApplicationContext extends ActivityContext {

    private String[] mainArgs;

    public ApplicationContext(String[] mainArgs) {
        this.mainArgs = mainArgs;
    }
}
