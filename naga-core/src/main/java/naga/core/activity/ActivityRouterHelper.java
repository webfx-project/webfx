package naga.core.activity;

import naga.core.routing.Router;
import naga.core.routing.RoutingContext;
import naga.core.util.async.Handler;
import naga.core.util.function.Converter;
import naga.core.util.function.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActivityRouterHelper {

    private final Router router;
    private final ActivityContext parentContext;
    private Converter<RoutingContext, ActivityContext> defaultContextConverter = this::convertRoutingContextToActivityContext;

    public ActivityRouterHelper(Router router, ActivityContext parentContext) {
        this.router = router;
        this.parentContext = parentContext;
    }

    public void setDefaultContextConverter(Converter<RoutingContext, ActivityContext> defaultContextConverter) {
        this.defaultContextConverter = defaultContextConverter;
    }

    /* GWT public <CT> ActivityRouterHelper route(String path, Class<? extends Activity<CT>> activityClass) {
        return route(path, activityClass, null);
    }

    public <CT> ActivityRouterHelper route(String path, Class<? extends Activity<CT>> activityClass, Converter<RoutingContext, CT> contextConverter) {
        return route(path, Factory.fromDefaultConstructor(activityClass), contextConverter);
    }*/

    public ActivityRouterHelper route(String path, Factory<Activity> activityFactory) {
        return route(path, activityFactory, null);
    }

    public ActivityRouterHelper route(String path, Factory<Activity> activityFactory, Converter<RoutingContext, ActivityContext> contextConverter) {
        if (contextConverter == null)
            contextConverter = defaultContextConverter;
        router.route(path, new ActivityRoutingHandler(ActivityManager.factory(activityFactory), contextConverter));
        return this;
    }

    private static class ActivityRoutingHandler implements Handler<RoutingContext> {

        private final Converter<RoutingContext, ActivityContext> contextConverter;
        private final Factory<ActivityManager> activityManagerFactory;
        private ActivityManager activityManager;

        ActivityRoutingHandler(Factory<ActivityManager> activityManagerFactory, Converter<RoutingContext, ActivityContext> contextConverter) {
            this.contextConverter = contextConverter;
            this.activityManagerFactory = activityManagerFactory;
        }

        @Override
        public void handle(RoutingContext context) {
            ActivityContext activityContext = contextConverter.convert(context);
            if (activityManager == null)
                activityManager = activityManagerFactory.create();
            activityManager.run(activityContext);
        }
    }

    private final Map<String, ActivityContext> history = new HashMap<>();

    private ActivityContext convertRoutingContextToActivityContext(RoutingContext routingContext) {
        ActivityContext activityContext = history.get(routingContext.path());
        if (activityContext == null)
            history.put(routingContext.path(), activityContext = new ActivityContext(parentContext));
        activityContext.setRouter(router);
        activityContext.setParams(routingContext.getParams());
        parentContext.nodeProperty().bind(activityContext.nodeProperty());
        return activityContext;
    }

}
