package webfx.framework.operations.route;

import webfx.framework.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.router.auth.authz.RouteRequest;
import webfx.platforms.core.util.collection.Collections;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public interface RouteRequestEmitter {

    RouteRequest instantiateRouteRequest(UiRouteActivityContext context);

    static Collection<RouteRequestEmitter> getProvidedEmitters() {
        return Collections.listOf(ServiceLoader.load(RouteRequestEmitter.class));
    }
}
