package webfx.framework.ui.uirouter;

import webfx.framework.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.router.RoutingContext;
import webfx.framework.router.util.PathBuilder;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContextFactory;
import webfx.platforms.core.util.function.Converter;
import webfx.platforms.core.util.function.Factory;

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
}
