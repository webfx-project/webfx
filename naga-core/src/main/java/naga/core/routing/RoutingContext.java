package naga.core.routing;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface RoutingContext {

    String path();

    void next();

    Map<String, String> getParams();

    void fail(int statusCode);

    void fail(Throwable throwable);

    String mountPoint();

    Route currentRoute();

    int statusCode();

    boolean failed();

    Throwable failure();

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
