package naga.framework.ui.router;

import naga.commons.util.async.Handler;
import naga.commons.util.function.Converter;
import naga.commons.util.function.Factory;
import naga.framework.activity.uiroute.UiRouteActivityContext;
import naga.framework.activity.uiroute.impl.UiRouteActivityContextBase;
import naga.framework.activity.view.HasMountNodeProperty;
import naga.framework.router.Router;
import naga.framework.router.RoutingContext;
import naga.fx.properties.markers.HasNodeProperty;
import naga.fx.spi.Toolkit;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityManager;
import naga.platform.activity.application.ApplicationContext;
import naga.platform.client.url.history.History;
import naga.platform.client.url.history.baseimpl.SubHistory;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class UiRouter extends HistoryRouter {

    private final UiRouteActivityContext hostingContext; // The activity context that hosts this router
    private final ActivityContextFactory activityContextFactory;
    // Fields used for sub routing
    private UiRouter mountParentRouter;    // pointer set on the child sub router to reference the parent router
    private UiRouter mountChildSubRouter;  // pointer set on the parent router to reference the child sub router
    private final Map<String, ActivityContext> activityContextHistory = new HashMap<>();

    public static UiRouter create(UiRouteActivityContext hostingContext) {
        return create(hostingContext, hostingContext.getActivityContextFactory());
    }

    public static UiRouter create(UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        return new UiRouter(hostingContext, activityContextFactory);
    }

    public static UiRouter createSubRouter(UiRouteActivityContext hostingContext) {
        return createSubRouter(hostingContext, hostingContext.getActivityContextFactory());
    }

    public static <C extends UiRouteActivityContext<C>> UiRouter createSubRouter(C hostingContext, ActivityContextFactory<C> activityContextFactory) {
        return UiRouter.create(createSubRouterContext(hostingContext, activityContextFactory));
    }

    private static <C extends UiRouteActivityContext<C>> C createSubRouterContext(C hostingContext, ActivityContextFactory<C> activityContextFactory) {
        // For now we just create a new context that is different from the parent router one.
        return activityContextFactory.createContext(hostingContext);
        // The main links between these 2 contexts will actually be done later:
        // - in routeAndMountSubRouter() which will reset the history to a SubHistory (to consider the mount point shift)
        // - in ActivityRoutingHandler.handle() which will bind the parent mount node to the sub router context node
        //   (so the sub activity appears in the appropriate place within the parent activity)
    }

    private UiRouter(UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        this(Router.create(), hostingContext, activityContextFactory);
    }

    private UiRouter(Router router, UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        this(router, hostingContext.getHistory(), hostingContext, activityContextFactory);
    }

    private UiRouter(Router router, History history, UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        super(router, history);
        this.hostingContext = hostingContext;
        this.activityContextFactory = activityContextFactory;
        UiRouteActivityContextBase hostingUiRouterActivityContext = UiRouteActivityContextBase.toUiRouterActivityContextBase(hostingContext);
        if (hostingUiRouterActivityContext != null) // can be null if the hosting context is the application context
            hostingUiRouterActivityContext.setUiRouter(this);
    }

    /* GWT public <CT> UiRouter route(String path, Class<? extends Activity<CT>> activityClass) {
        return route(path, activityClass, null);
    }

    public <CT> UiRouter route(String path, Class<? extends Activity<CT>> activityClass, Converter<RoutingContext, CT> contextConverter) {
        return route(path, Factory.fromDefaultConstructor(activityClass), contextConverter);
    }*/

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory) {
        return route(path, activityFactory, activityContextFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(path, activityFactory, activityContextFactory, null);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        router.route(path, new ActivityRoutingHandler<>(ActivityManager.factory(activityFactory, activityContextFactory), contextConverter));
        return this;
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAndMount(String path, Factory<Activity<C>> activityFactory, UiRouter subRouter) {
        return routeAndMount(path, activityFactory, activityContextFactory, subRouter);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAndMount(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, UiRouter subRouter) {
        // Mounting the sub router to make its activities findable by the current router
        router.mountSubRouter(path, subRouter.router);
        // Also adding the route to the current activity to make it findable (this is the current activity that finally will display the sub activities in it)
        route(path, activityFactory, activityContextFactory);
        // Memorizing the link from the sub router to this router (for the sub routing management in ActivityRoutingHandler)
        subRouter.mountParentRouter = this;
        // Also changing the sub router history so that when sub activities call history.push("/xxx"), they actually do history.push("/{path}/xxx")
        subRouter.setHistory(new SubHistory(subRouter.getHistory(), path));
        return this;
    }

    // Was originally in ActivityRoutingHandler but was moved in upper level because otherwise activities were not paused
    private ActivityManager activityManager; // TODO: check if this is correct to put it here

    private class ActivityRoutingHandler<C extends UiRouteActivityContext<C>> implements Handler<RoutingContext> {

        private final Converter<RoutingContext, C> contextConverter;
        private final Factory<ActivityManager<C>> activityManagerFactory;

        ActivityRoutingHandler(Factory<ActivityManager<C>> activityManagerFactory, Converter<RoutingContext, C> contextConverter) {
            this.contextConverter = contextConverter != null ? contextConverter : this::convertRoutingContextToActivityContext;
            this.activityManagerFactory = activityManagerFactory;
        }

        @Override
        public void handle(RoutingContext context) {
            // Creating or retrieving the activity context associated with the requested routing context
            C activityContext = contextConverter.convert(context);
            // Since we will switch the activity, the current activity and its manager will now become the previous ones
            ActivityManager<C> previousActivityManager = activityManager; // let's memorize the reference to it
            // Now we switch the current activity and its manager to the current one
            activityManager = activityContext.getActivityManager();
            // The returned value is not null only if we switched back to an already existing activity that has been paused before
            if (activityManager == null) { // otherwise, this is the first time we switch to this activity which is therefore not yet created
                activityManager = activityManagerFactory.create(); // So we create the activity manager (and its associated activity)
                activityManager.create(activityContext); // and we transit the activity into the create state and pass the context
            }
            // Now that the new requested activity is displayed, we pause the previous activity
            if (previousActivityManager != null) // if there was a previous activity
                previousActivityManager.pause();
            // Now we transit the current activity (which was either paused or newly created) into the resume state and
            // once done we display the activity node by binding it with the hosting context (done in the UI tread)
            activityManager.resume().setHandler(event -> {
                if (hostingContext instanceof HasNodeProperty && activityContext instanceof HasNodeProperty)
                    Toolkit.get().scheduler().runInUiThread(() ->
                        ((HasNodeProperty) hostingContext).nodeProperty().bind(((HasNodeProperty) activityContext).nodeProperty())
                    );
            });
            /*--- Sub routing management ---*/
            // When the activity is a mount child activity coming from sub routing, we make sure the mount parent activity is displayed
            if (mountParentRouter != null) { // Indicates it is a child sub router
                mountParentRouter.mountChildSubRouter = UiRouter.this; // Setting the parent router child pointer
                // Calling the parent router on the mount point will cause the parent activity to be displayed (if not already done)
                mountParentRouter.router.accept(context.mountPoint() + "/", context.getParams());
            }
            // When the activity is a mount parent activity, we makes the trick so the child activity is displayed within the parent activity
            if (mountChildSubRouter != null) // Indicates it is a mount parent activity
                // The trick is to bind the mount node of the parent activity to the child activity node
                if (activityContext instanceof HasMountNodeProperty && mountChildSubRouter.hostingContext instanceof HasNodeProperty)
                    Toolkit.get().scheduler().runInUiThread(() ->
                        ((HasMountNodeProperty) activityContext).mountNodeProperty().bind(((HasNodeProperty) mountChildSubRouter.hostingContext).nodeProperty()) // Using the hosting context node which is bound to the child activity node
                    );
                // This should display the child activity because a mount parent activity is supposed to bind its context mount node to the UI
        }

        private C convertRoutingContextToActivityContext(RoutingContext routingContext) {
            String contextKey = routingContext.currentRoute().getPath();
            C activityContext = (C) activityContextHistory.get(contextKey);
            if (activityContext == null)
                activityContextHistory.put(contextKey, activityContext = activityManagerFactory.create().getContextFactory().createContext(hostingContext));
            applyRoutingContextParamsToActivityContext(routingContext.getParams(), activityContext);
            return activityContext;
        }

        private void applyRoutingContextParamsToActivityContext(JsonObject routingContextParams, C activityContext) {
            // Temporary applying the parameters to the whole application context so they can be shared between activities
            // (ex: changing :x parameter in activity1 and then pressing a navigation button in a parent container activity
            // that goes to /:x/activity2 => the parent container can get the last :x value changed by activity1)
            //UiRouteActivityContextBase.from(activityContext).setParams(routingContext.getParams()); // Commented original code
            UiRouteActivityContext uiAppContext = ApplicationContext.get();
            WritableJsonObject appParams = (WritableJsonObject) uiAppContext.getParams();
            // TODO: move this code into a apply() method in WritableJsonObject
            JsonArray keys = routingContextParams.keys();
            for (int i = 0, size = keys.size(); i < size; i++) {
                String key = keys.getString(i);
                appParams.setNativeElement(key, routingContextParams.getNativeElement(key));
            }
        }
    }
}
