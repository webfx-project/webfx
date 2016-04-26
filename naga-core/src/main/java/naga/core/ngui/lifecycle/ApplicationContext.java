package naga.core.ngui.lifecycle;

/**
 * @author Bruno Salmon
 */
public class ApplicationContext implements CyclicalContext {

    private String[] mainArgs;

    public ApplicationContext(String[] mainArgs) {
        this.mainArgs = mainArgs;
    }
}
