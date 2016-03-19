package naga.core.ngui.routing;

import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class UiRoute {

    private final UiRouter router;
    private boolean added;
    private String path;
    private Handler<UiRoutingContext> handler;

    public UiRoute(UiRouter router) {
        this.router = router;
    }

    UiRoute path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return path;
    }

    public UiRoute handler(Handler<UiRoutingContext> handler) {
        this.handler = handler;
        checkAdd();
        return this;
    }

    boolean matches(UiRoutingContext context) {
        return path.equals(context.path());
    }

    void handleContext(UiRoutingContext context) {
        if (handler != null)
            handler.handle(context);
    }

    private void checkAdd() {
        if (!added) {
            router.addRoute(this);
            added = true;
        }
    }

    /*
    UiRoute name(String name);

    String name();

    UiRoute parent(UiRoute parent);

    UiRoute parent();


    UiRoute pathRegex(String path);

    UiRoute useNormalisedPath(boolean useNormalisedPath);

    String getPath();

    UiRoute order(int order);

    UiRoute last();

    UiRoute disable();

    UiRoute enable();

    UiRoute remove();

    UiRoute handler(Handler<UiRoutingContext> handler);

    */


}
