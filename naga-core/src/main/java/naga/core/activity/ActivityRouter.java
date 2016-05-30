package naga.core.activity;

import naga.core.routing.HistoryRouter;
import naga.core.routing.history.History;
import naga.core.routing.router.Router;
import naga.core.routing.router.RoutingContext;
import naga.core.spi.toolkit.Toolkit;
import naga.core.util.async.Handler;
import naga.core.util.function.Converter;
import naga.core.util.function.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActivityRouter extends HistoryRouter {

    private final ActivityContext parentContext;
    private Converter<RoutingContext, ActivityContext> defaultContextConverter = this::convertRoutingContextToActivityContext;

    public ActivityRouter(ActivityContext parentContext) {
        this(null, parentContext);
    }

    public ActivityRouter(Router router, ActivityContext parentContext) {
        this(router, null, parentContext);
    }

    public ActivityRouter(Router router, History history, ActivityContext parentContext) {
        super(router, history);
        this.parentContext = parentContext;
    }

    public void setDefaultContextConverter(Converter<RoutingContext, ActivityContext> defaultContextConverter) {
        this.defaultContextConverter = defaultContextConverter;
    }

    /* GWT public <CT> ActivityRouter route(String path, Class<? extends Activity<CT>> activityClass) {
        return route(path, activityClass, null);
    }

    public <CT> ActivityRouter route(String path, Class<? extends Activity<CT>> activityClass, Converter<RoutingContext, CT> contextConverter) {
        return route(path, Factory.fromDefaultConstructor(activityClass), contextConverter);
    }*/

    public ActivityRouter route(String path, Factory<Activity> activityFactory) {
        return route(path, activityFactory, null);
    }

    public ActivityRouter route(String path, Factory<Activity> activityFactory, Converter<RoutingContext, ActivityContext> contextConverter) {
        router.route(path, new ActivityRoutingHandler(ActivityManager.factory(activityFactory), contextConverter));
        return this;
    }

    private class ActivityRoutingHandler implements Handler<RoutingContext> {

        private final Converter<RoutingContext, ActivityContext> contextConverter;
        private final Factory<ActivityManager> activityManagerFactory;
        private ActivityManager activityManager;

        ActivityRoutingHandler(Factory<ActivityManager> activityManagerFactory, Converter<RoutingContext, ActivityContext> contextConverter) {
            this.contextConverter = contextConverter != null ? contextConverter : defaultContextConverter;
            this.activityManagerFactory = activityManagerFactory;
        }

        @Override
        public void handle(RoutingContext context) {
            // Creating or retrieving the activity context associated with the requested routing context
            ActivityContext activityContext = contextConverter.convert(context);
            // Since we will switch the activity, the current activity and its manager will now become the previous ones
            ActivityManager previousActivityManager = activityManager; // let's memorize the reference to it
            // Now we switch the current activity and its manager to the current one
            activityManager = activityContext.getActivityManager();
            // The returned value is not null only if we switched back to an already existing activity that has been paused before
            if (activityManager == null) { // otherwise, this is the first time we switch to this activity which is therefore not yet created
                activityManager = activityManagerFactory.create(); // So we create the activity manager (and its associated activity)
                activityManager.create(activityContext); // and we transit the activity into the create state and pass the context
            }
            // Now we transit the current activity (which was either paused or newly created) into the resume state and
            // once done we display the activity node by binding it with the parent context (done in the UI tread)
            activityManager.resume().setHandler(event -> Toolkit.get().scheduler().runInUiThread(() -> parentContext.nodeProperty().bind(activityContext.nodeProperty())));
            // Now that the new requested activity is displayed, we pause the previous activity
            if (previousActivityManager != null) // if there was a previous activity
                previousActivityManager.pause();
        }
    }

    private final Map<String, ActivityContext> activityContextHistory = new HashMap<>();

    private ActivityContext convertRoutingContextToActivityContext(RoutingContext routingContext) {
        String contextKey = routingContext.currentRoute().getPath();
        ActivityContext activityContext = activityContextHistory.get(contextKey);
        if (activityContext == null)
            activityContextHistory.put(contextKey, activityContext = new ActivityContext(parentContext));
        activityContext.setActivityRouter(this);
        activityContext.setParams(routingContext.getParams());
        return activityContext;
    }

}
