package webfx.framework.client.operations.route;

import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.shared.router.auth.authz.RouteRequest;
import webfx.platform.shared.util.collection.Collections;

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
