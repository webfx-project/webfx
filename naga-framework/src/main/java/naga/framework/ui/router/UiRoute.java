package naga.framework.ui.router;

import naga.framework.activity.uiroute.UiRouteActivityContext;
import naga.framework.router.RoutingContext;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityContextFactory;
import naga.util.function.Converter;
import naga.util.function.Factory;

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
}
