package naga.core.routing;

import naga.core.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Router {

    private String currentPath;
    private String defaultPath;
    private List<Route> routes = new ArrayList<>();

    public Route route(String path) {
        return new Route(this).path(path);
    }

    public Router route(String path, Handler<RoutingContext> handler) {
        new Route(this).path(path).handler(handler);
        return this;
    }

    void addRoute(Route route) {
        routes.add(route);
    }

    public Router defaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
        return this;
    }

    public Router start() {
        if (currentPath == null)
            currentPath = defaultPath;
        if (currentPath == null && !routes.isEmpty())
            currentPath = routes.get(0).path();
        accept(currentPath);
        return this;
    }

    public void accept(String path) {
        this.currentPath = path;
        new RoutingContext(path, routes).next();
    }


    /*
    Router stop();

    Router goTo(Route route);

    Router goTo(String routeName);

    Router go(int delta);

    default Router back() { return go(-1); }

    default Router forward() { return go(1); }

    int historyLength();
    */
}
