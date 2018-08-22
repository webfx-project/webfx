package naga.framework.ui.uirouter.impl;

import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import naga.framework.router.RoutingContext;
import naga.framework.ui.uirouter.UiRoute;
import naga.framework.activity.Activity;
import naga.framework.activity.ActivityContextFactory;
import naga.util.function.Converter;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public final class UiRouteImpl<C extends UiRouteActivityContext<C>> implements UiRoute<C> {

    private final String path;
    private final boolean regex;
    private final boolean auth;
    private final Factory<Activity<C>> activityFactory;
    private final ActivityContextFactory<C> activityContextFactory;
    private final Converter<RoutingContext, C> contextConverter;

    public UiRouteImpl(String path, boolean regex, boolean auth, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        this.path = path;
        this.regex = regex;
        this.auth = auth;
        this.activityFactory = activityFactory;
        this.activityContextFactory = activityContextFactory;
        this.contextConverter = contextConverter;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isRegex() {
        return regex;
    }

    @Override
    public boolean requiresAuthentication() {
        return auth;
    }

    @Override
    public Factory<Activity<C>> activityFactory() {
        return activityFactory;
    }

    @Override
    public ActivityContextFactory<C> activityContextFactory() {
        return activityContextFactory;
    }

    @Override
    public Converter<RoutingContext, C> contextConverter() {
        return contextConverter;
    }
}
