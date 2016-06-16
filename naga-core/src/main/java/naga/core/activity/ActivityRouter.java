package naga.core.activity;

import naga.core.routing.HistoryRouter;
import naga.core.routing.history.History;
import naga.core.routing.history.impl.SubHistory;
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

    private final ActivityContext hostingContext; // The activity context that hosts this router
    private Converter<RoutingContext, ActivityContext> defaultContextConverter = this::convertRoutingContextToActivityContext;
    // Fields used for sub routing
    private ActivityRouter mountParentRouter;    // pointer set on the child sub router to reference the parent router
    private ActivityRouter mountChildSubRouter;  // pointer set on the parent router to reference the child sub router

    public static ActivityRouter create(ActivityContext hostingContext) {
        return new ActivityRouter(hostingContext);
    }

    public static ActivityRouter createSubRouter(ActivityContext hostingContext) {
        return ActivityRouter.create(createSubRouterContext(hostingContext));
    }

    public static ActivityContext createSubRouterContext(ActivityContext hostingContext) {
        return new ActivityContext(hostingContext);
    }

    public ActivityRouter(ActivityContext hostingContext) {
        this(Router.create(), hostingContext);
    }

    public ActivityRouter(Router router, ActivityContext hostingContext) {
        this(router, hostingContext.getHistory(), hostingContext);
    }

    public ActivityRouter(Router router, History history, ActivityContext hostingContext) {
        super(router, history);
        this.hostingContext = hostingContext;
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

    public ActivityRouter routeAndMountSubRouter(String path, Factory<Activity> activityFactory, ActivityRouter subRouter) {
        // Mounting the sub router to make its activities findable by the current router
        router.mountSubRouter(path, subRouter.router);
        // Also adding the route to the current activity to make it findable (this is the current activity that finally will display the sub activities in it)
        route(path, activityFactory);
        // Memorizing the link from the sub router to this router (for the sub routing management in ActivityRoutingHandler)
        subRouter.mountParentRouter = this;
        // Also changing the sub router history so that when sub activities call history.push("/xxx"), they actually do history.push("/{path}/xxx")
        subRouter.hostingContext.setHistory(new SubHistory(hostingContext.getHistory(), path));
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
            // once done we display the activity node by binding it with the hosting context (done in the UI tread)
            activityManager.resume().setHandler(event -> Toolkit.get().scheduler().runInUiThread(() -> hostingContext.nodeProperty().bind(activityContext.nodeProperty())));
            // Now that the new requested activity is displayed, we pause the previous activity
            if (previousActivityManager != null) // if there was a previous activity
                previousActivityManager.pause();
            /*** Sub routing management ***/
            // When the activity is a mount child activity coming from sub routing, we make sure the mount parent activity is displayed
            if (mountParentRouter != null) { // Indicates it is a child sub router
                mountParentRouter.mountChildSubRouter = ActivityRouter.this; // Setting the parent router child pointer
                // Calling the parent router on the mount point will cause the parent activity to be displayed (if not already done)
                mountParentRouter.router.accept(context.mountPoint() + "/", context.getParams());
            }
            // When the activity is a mount parent activity, we makes the trick so the child activity is displayed within the parent activity
            if (mountChildSubRouter != null) // Indicates it is a mount parent activity
                // The trick is to bind the mount node of the parent activity to the child activity node
                activityContext.mountNodeProperty().bind(mountChildSubRouter.hostingContext.nodeProperty()); // Using the hosting context node which is bound to the child activity node
                // This should display the child activity because a mount parent activity is supposed to bind its context mount node to the UI
        }
    }

    private final Map<String, ActivityContext> activityContextHistory = new HashMap<>();

    private ActivityContext convertRoutingContextToActivityContext(RoutingContext routingContext) {
        String contextKey = routingContext.currentRoute().getPath();
        ActivityContext activityContext = activityContextHistory.get(contextKey);
        if (activityContext == null)
            activityContextHistory.put(contextKey, activityContext = new ActivityContext(hostingContext));
        activityContext.setParams(routingContext.getParams());
        return activityContext;
    }

}
