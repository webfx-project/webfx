package naga.core.ngui.routing;

import naga.core.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class UiRouter {

    private static UiRouter SINGLETON = new UiRouter();

    private String currentPath;
    private String defaultPath;
    private List<UiRoute> routes = new ArrayList<>();

    private UiRouter() {
    }

    public static UiRouter get() {
        return SINGLETON;
    }

    public UiRoute route(String path) {
        return new UiRoute(this).path(path);
    }

    public UiRouter route(String path, Handler<UiRoutingContext> handler) {
        new UiRoute(this).path(path).handler(handler);
        return this;
    }

    void addRoute(UiRoute route) {
        routes.add(route);
    }

    public UiRouter defaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
        return this;
    }

    public UiRouter start() {
        if (currentPath == null)
            currentPath = defaultPath;
        if (currentPath == null && !routes.isEmpty())
            currentPath = routes.get(0).path();
        accept(currentPath);
        return this;
    }

    public void accept(String path) {
        this.currentPath = path;
        new UiRoutingContext(path, routes).next();
    }


    /*
    UiRouter stop();

    UiRouter goTo(UiRoute route);

    UiRouter goTo(String routeName);

    UiRouter go(int delta);

    default UiRouter back() { return go(-1); }

    default UiRouter forward() { return go(1); }

    int historyLength();
    */
}
