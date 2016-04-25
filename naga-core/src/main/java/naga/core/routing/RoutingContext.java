package naga.core.routing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class RoutingContext {

    private final String path;
    //private final Collection<Route> routes;
    private final Iterator<Route> iterator;
    private Route currentRoute;
    private Map<String, String> params;

    public RoutingContext(String path, Collection<Route> routes) {
        this.path = path;
        //this.routes = routes;
        iterator = routes.iterator();
    }

    public String path() {
        return path;
    }

    public Route currentRoute() {
        return currentRoute;
    }

    void next() {
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.matches(this)) {
                currentRoute = route;
                route.handleContext(this);
            }
        }
    }

    public Map<String, String> getParams() {
        if (params == null)
            params = new HashMap<>();
        return params;
    }

    /*

    String normalisedPath();

    Cookie getCookie(String name);

    RoutingContext addCookie(Cookie cookie);

    Cookie removeCookie(String name);

    int cookieCount();

    Set<Cookie> cookies();

    Session session();

    User user();

    void setSession(Session session);

    void setUser(User user);

    void clearUser();

    */

}
