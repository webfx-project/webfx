package webfx.framework.client.ui.uirouter;

import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;
import webfx.framework.client.activity.ActivityManager;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.activity.impl.elementals.uiroute.impl.UiRouteActivityContextBase;
import webfx.framework.client.activity.impl.elementals.view.HasMountNodeProperty;
import webfx.framework.client.activity.impl.elementals.view.HasNodeProperty;
import webfx.framework.client.ui.uirouter.uisession.UiSession;
import webfx.framework.shared.router.Route;
import webfx.framework.shared.router.Router;
import webfx.framework.shared.router.RoutingContext;
import webfx.framework.shared.router.auth.RedirectAuthHandler;
import webfx.framework.shared.router.session.SessionHandler;
import webfx.framework.shared.router.session.SessionStore;
import webfx.framework.shared.router.session.UserSessionHandler;
import webfx.framework.shared.router.session.impl.MemorySessionStore;
import webfx.framework.shared.router.session.impl.UserHolder;
import webfx.framework.shared.router.session.impl.UserSessionHandlerImpl;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.client.services.windowhistory.spi.impl.SubBrowsingHistory;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.function.Converter;
import webfx.platform.shared.util.function.Factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class UiRouter extends HistoryRouter {

    private final UiRouteActivityContext hostingContext; // The activity context that hosts this router
    private final ActivityContextFactory activityContextFactory;
    // Fields used for sub routing
    private UiRouter mountParentRouter;    // pointer set on the child sub router to reference the parent router
    private UiRouter mountChildSubRouter;  // pointer set on the parent router to reference the child sub router
    private final Map<String, ActivityContext> activityContextHistory = new HashMap<>();
    // Auth
    private SessionStore sessionStore;
    private static String sessionId;
    private RedirectAuthHandler redirectAuthHandler;
    private UiSession uiSession;

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
        // - in routeAndMountSubRouter() which will reset the history to a SubBrowsingHistory (to consider the mount point shift)
        // - in ActivityRoutingHandler.handle() which will bind the parent mount node to the sub router context node
        //   (so the sub activity appears in the appropriate place within the parent activity)
    }

    private UiRouter(UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        this(Router.create(), hostingContext, activityContextFactory);
    }

    private UiRouter(Router router, UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        this(router, hostingContext.getHistory(), hostingContext, activityContextFactory);
    }

    private UiRouter(Router router, BrowsingHistory browsingHistory, UiRouteActivityContext hostingContext, ActivityContextFactory activityContextFactory) {
        super(router, browsingHistory);
        this.hostingContext = hostingContext;
        this.activityContextFactory = activityContextFactory;
        UiRouteActivityContextBase hostingUiRouterActivityContext = UiRouteActivityContextBase.toUiRouterActivityContextBase(hostingContext);
        if (hostingUiRouterActivityContext != null) // can be null if the hosting context is the application context
            hostingUiRouterActivityContext.setUiRouter(this);
        setRouterSessionAndUserHandlers();
    }

    private void setRouterSessionAndUserHandlers() {
        router.route().handler(SessionHandler.create(this::getSessionStore, () -> sessionId, id -> sessionId = id));
        router.route().handler(UserSessionHandler.create());
    }

    @Override
    public void refresh() {
        if (mountParentRouter == null)
            super.refresh();
        else
            mountParentRouter.refresh();
    }

    public UiRouter setRedirectAuthHandler(String loginPath, String unauthorizedPath) {
        return setRedirectAuthHandler(RedirectAuthHandler.create(loginPath, unauthorizedPath));
    }

    public UiRouter setRedirectAuthHandler(RedirectAuthHandler redirectAuthHandler) {
        // The redirectAuthHandler is not added now but will be added on each route that needs to be secured using addAuthorizationRouteCheck()
        this.redirectAuthHandler = redirectAuthHandler; // So just keeping the reference for now
        return this;
    }

    private void addAuthorizationRouteCheck(String authPath, boolean regex) {
        if (redirectAuthHandler == null)
            throw new IllegalStateException("setRedirectAuthHandler() must be called on this router before calling authRoute()");
        Route route = regex ? router.routeWithRegex(authPath) : router.route(authPath);
        route.handler(redirectAuthHandler);
    }

    private SessionStore getSessionStore() {
        if (sessionStore == null) {
            if (mountParentRouter != null)
                sessionStore = mountParentRouter.getSessionStore();
            else
                sessionStore = MemorySessionStore.create();
        }
        return sessionStore;
    }

    public UiSession getUiSession() {
        if (uiSession == null) {
            if (mountParentRouter != null)
                return mountParentRouter.getUiSession();
            uiSession = UiSession.get();
            uiSession.userPrincipalProperty().addListener((observable, oldUser, userPrincipal) -> getSessionStore().get(sessionId).setHandler(ar -> {
                if (ar.succeeded()) {
                    UserSessionHandlerImpl.setSessionUserHolder(ar.result(), new UserHolder(userPrincipal));
                    refresh();
                }
            }));
        }
        return uiSession;
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAuth(String path, Factory<Activity<C>> activityFactory) {
        return route(true, path, activityFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAuth(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(true, false, path, activityFactory, activityContextFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAuth(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        return route(true, false, path, activityFactory, activityContextFactory, contextConverter);
    }

    /* GWT public <CT> UiRouter route(String path, Class<? extends Activity<CT>> activityClass) {
        return route(path, activityClass, null);
    }

    public <CT> UiRouter route(String path, Class<? extends Activity<CT>> activityClass, Converter<RoutingContext, CT> contextConverter) {
        return route(path, Factory.fromDefaultConstructor(activityClass), contextConverter);
    }*/

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory) {
        return route(false, path, activityFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(false, false, path, activityFactory, activityContextFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeWithRegex(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(false, true, path, activityFactory, activityContextFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter routeAuthWithRegex(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(true, true, path, activityFactory, activityContextFactory);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter route(String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        return route(false, false, path, activityFactory, activityContextFactory, contextConverter);
    }

    private <C extends UiRouteActivityContext<C>> UiRouter route(boolean auth, String path, Factory<Activity<C>> activityFactory) {
        return route(auth, false, path, activityFactory, activityContextFactory);
    }

    private <C extends UiRouteActivityContext<C>> UiRouter route(boolean auth, boolean regex, String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory) {
        return route(auth, regex, path, activityFactory, activityContextFactory, null);
    }

    public <C extends UiRouteActivityContext<C>> UiRouter route(UiRoute<C> uiRoute) {
        return route(uiRoute.requiresAuthentication(), uiRoute.isRegex(), uiRoute.getPath(), uiRoute.activityFactory(), uiRoute.activityContextFactory(), uiRoute.contextConverter());
    }

    public <C extends UiRouteActivityContext<C>> UiRouter registerProvidedUiRoutes() {
        Collection<UiRoute> providedUiRoutes = UiRoute.getProvidedUiRoutes();
        if (redirectAuthHandler == null) {
            UiRoute loginUiRoute = Collections.findFirst(providedUiRoutes, uiRoute -> uiRoute instanceof ProvidedLoginUiRoute);
            UiRoute unauthorizedUiRoute = Collections.findFirst(providedUiRoutes, uiRoute -> uiRoute instanceof ProvidedUnauthorizedUiRoute);
            if (loginUiRoute != null && unauthorizedUiRoute != null)
                setRedirectAuthHandler(loginUiRoute.getPath(), unauthorizedUiRoute.getPath());
        }
        StringBuilder sb = new StringBuilder("***** Registered the following provided ui routes: ****");
        providedUiRoutes.forEach(uiRoute -> {
            sb.append("\n").append(uiRoute.getPath());
            route(uiRoute);
        });
        Logger.log(sb.append("\n").append("*******************************************************"));
        return this;
    }

    private <C extends UiRouteActivityContext<C>> UiRouter route(boolean auth, boolean regex, String path, Factory<Activity<C>> activityFactory, ActivityContextFactory<C> activityContextFactory, Converter<RoutingContext, C> contextConverter) {
        if (auth)
            addAuthorizationRouteCheck(path, regex);
        ActivityRoutingHandler<C> handler = new ActivityRoutingHandler<>(ActivityManager.factory(activityFactory, activityContextFactory), contextConverter);
        if (regex)
            router.routeWithRegex(path, handler);
        else
            router.route(path, handler);
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
        subRouter.setHistory(new SubBrowsingHistory(subRouter.getHistory(), path));
        return this;
    }

    // Was originally in ActivityRoutingHandler but was moved in upper level because otherwise activities were not paused
    private ActivityManager activityManager; // TODO: check if this is correct to put it here

    private final class ActivityRoutingHandler<C extends UiRouteActivityContext<C>> implements Handler<RoutingContext> {

        private final Converter<RoutingContext, C> contextConverter;
        private final Factory<ActivityManager<C>> activityManagerFactory;

        ActivityRoutingHandler(Factory<ActivityManager<C>> activityManagerFactory, Converter<RoutingContext, C> contextConverter) {
            this.contextConverter = contextConverter != null ? contextConverter : this::convertRoutingContextToActivityContext;
            this.activityManagerFactory = activityManagerFactory;
        }

        @Override
        public void handle(RoutingContext routingContext) {
            // Creating or retrieving the activity context associated with the requested routing context
            C activityContext = contextConverter.convert(routingContext);
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
                    UiScheduler.runInUiThread(() ->
                        ((HasNodeProperty) hostingContext).nodeProperty().bind(((HasNodeProperty) activityContext).nodeProperty())
                    );
            });
            /*--- Sub routing management ---*/
            // When the activity is a mount child activity coming from sub routing, we make sure the mount parent activity is displayed
            if (mountParentRouter != null) { // Indicates it is a child sub router
                mountParentRouter.mountChildSubRouter = UiRouter.this; // Setting the parent router child pointer
                // Calling the parent router on the mount point will cause the parent activity to be displayed (if not already done)
                mountParentRouter.router.accept(routingContext.mountPoint() + "/", routingContext.getParams());
            }
            // When the activity is a mount parent activity, we makes the trick so the child activity is displayed within the parent activity
            if (mountChildSubRouter != null) // Indicates it is a mount parent activity
                // The trick is to bind the mount node of the parent activity to the child activity node
                if (activityContext instanceof HasMountNodeProperty && mountChildSubRouter.hostingContext instanceof HasNodeProperty)
                    UiScheduler.runInUiThread(() ->
                        ((HasMountNodeProperty) activityContext).mountNodeProperty().bind(((HasNodeProperty) mountChildSubRouter.hostingContext).nodeProperty()) // Using the hosting context node which is bound to the child activity node
                    );
                // This should display the child activity because a mount parent activity is supposed to bind its context mount node to the UI
        }

        private C convertRoutingContextToActivityContext(RoutingContext routingContext) {
            String contextKey = routingContext.currentRoute().getPath();
            C activityContext = (C) activityContextHistory.get(contextKey);
            if (activityContext == null) {
                Logger.log("Creating activity context for " + contextKey);
                activityContextHistory.put(contextKey, activityContext = activityManagerFactory.create().getContextFactory().createContext(hostingContext));
            }
            applyRoutingContextParamsToActivityContext(routingContext.getParams(), activityContext);
            UiRouteActivityContextBase contextBase = UiRouteActivityContextBase.toUiRouterActivityContextBase(activityContext);
            contextBase.setRoutingPath(routingContext.path());
            contextBase.setSession(routingContext.session());
            return activityContext;
        }

        private void applyRoutingContextParamsToActivityContext(JsonObject routingContextParams, C activityContext) {
            // Temporary applying the parameters to the whole application context so they can be shared between activities
            // (ex: changing :x parameter in activity1 and then pressing a navigation button in a parent container activity
            // that goes to /:x/activity2 => the parent container can get the last :x value changed by activity1)
            WritableJsonObject localParams = null;
            //UiRouteActivityContext uiAppContext = ApplicationContext.get();
            //WritableJsonObject appParams = (WritableJsonObject) uiAppContext.getParams();
            JsonArray keys = routingContextParams.keys();
            for (int i = 0, size = keys.size(); i < size; i++) {
                String key = keys.getString(i);
                Object value = routingContextParams.getNativeElement(key);
                boolean localParameter = true; //"refresh".equals(key);
                /*if (!localParameter)
                    appParams.setNativeElement(key, value);
                else*/ {
                    if (localParams == null)
                        localParams = Json.createObject();
                    localParams.set(key, value);
                }
            }
            if (localParams != null)
                UiRouteActivityContextBase.toUiRouterActivityContextBase(activityContext).setParams(localParams);
        }
    }
}
