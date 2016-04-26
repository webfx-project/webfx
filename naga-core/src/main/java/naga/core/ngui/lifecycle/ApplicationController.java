package naga.core.ngui.lifecycle;

/**
 * @author Bruno Salmon
 */
public class ApplicationController extends CyclicalController<Application, ApplicationContext> {

    public ApplicationController(Application application, ApplicationContext context) {
        super(application, context);
    }

    public static void startApplication(Application application, String[] args) {
        new ApplicationController(application, new ApplicationContext(args)).start();
    }

}
