package webfx.framework.client.ui.uirouter;

import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.ActivityContextFactory;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.shared.router.RoutingContext;
import webfx.framework.shared.router.util.PathBuilder;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.function.Converter;
import webfx.platform.shared.util.function.Factory;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public interface UiRoute<C extends UiRouteActivityContext<C>> {

    String getPath();

    boolean isRegex();

    default boolean requiresAuthentication() {
        return false;
    }

    Factory<Activity<C>> activityFactory();

    default ActivityContextFactory<C> activityContextFactory() {
        return null;
    }

    default Converter<RoutingContext, C> contextConverter() {
        return null;
    }


    static <C extends UiRouteActivityContext<C>> UiRoute<C> create(String path, boolean auth, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return create(path, false, auth, activityFactory, activityContextFactory, null);
    }

    static <C extends UiRouteActivityContext<C>> UiRoute<C> createRegex(String path, boolean auth, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return create(PathBuilder.toRegexPath(path), true, auth, activityFactory, activityContextFactory, null);
    }

    static <C extends UiRouteActivityContext<C>> UiRoute<C> create(String path, boolean regex, boolean auth, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return create(path, regex, auth, activityFactory, activityContextFactory, null);
    }

    static <C extends UiRouteActivityContext<C>> UiRoute<C> create(String path, boolean regex, boolean auth, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        return new UiRouteImpl<>(path, regex, auth, activityFactory, activityContextFactory, contextConverter);
    }

    static Collection<UiRoute> getProvidedUiRoutes() {
        return Collections.listOf(ServiceLoader.load(UiRoute.class));
    }
}
