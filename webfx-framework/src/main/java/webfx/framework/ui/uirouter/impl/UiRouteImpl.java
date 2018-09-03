package webfx.framework.ui.uirouter.impl;

import webfx.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.router.RoutingContext;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContextFactory;
import webfx.platforms.core.util.function.Converter;
import webfx.platforms.core.util.function.Factory;

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
