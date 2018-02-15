package naga.framework.router;

import naga.framework.session.Session;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.auth.spi.authz.User;

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

    User user();

    void setSession(Session session);

    void setUser(User user);

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
