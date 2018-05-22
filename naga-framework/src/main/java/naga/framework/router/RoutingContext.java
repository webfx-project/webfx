package naga.framework.router;

import naga.framework.session.Session;
import naga.platform.services.json.WritableJsonObject;

/**
 * @author Bruno Salmon
 */
public interface RoutingContext {

    String path();

    void next();

    WritableJsonObject getParams();

    void fail(int statusCode);

    void fail(Throwable throwable);

    String mountPoint();

    Route currentRoute();

    int statusCode();

    boolean failed();

    Throwable failure();

    Session session();

    void setSession(Session session);

    Object userPrincipal();

    void setUserPrincipal(Object userPrincipal);

    void clearUser();

    /*

    String normalisedPath();

    Cookie getCookie(String name);

    RoutingContext addCookie(Cookie cookie);

    Cookie removeCookie(String name);

    int cookieCount();

    Set<Cookie> cookies();

    */

}
