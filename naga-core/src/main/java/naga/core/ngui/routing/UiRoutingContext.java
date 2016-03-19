package naga.core.ngui.routing;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public class UiRoutingContext {

    private final String path;
    //private final Collection<UiRoute> routes;
    private final Iterator<UiRoute> iterator;
    private UiRoute currentRoute;

    public UiRoutingContext(String path, Collection<UiRoute> routes) {
        this.path = path;
        //this.routes = routes;
        iterator = routes.iterator();
    }

    public String path() {
        return path;
    }

    public UiRoute currentRoute() {
        return currentRoute;
    }

    void next() {
        while (iterator.hasNext()) {
            UiRoute route = iterator.next();
            if (route.matches(this)) {
                currentRoute = route;
                route.handleContext(this);
            }
        }
    }

    /*

    String normalisedPath();

    Cookie getCookie(String name);

    UiRoutingContext addCookie(Cookie cookie);

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
