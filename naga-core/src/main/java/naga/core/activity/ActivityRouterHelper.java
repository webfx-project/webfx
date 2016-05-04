package naga.core.activity;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.core.routing.Router;
import naga.core.routing.RoutingContext;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.util.async.Handler;
import naga.core.util.function.Converter;
import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class ActivityRouterHelper {

    private final Router router;
    private final Converter<RoutingContext, ActivityContext> defaultContextConverter;

    public ActivityRouterHelper(Router router) {
        this(router, ActivityRouterHelper::convertRoutingContextToActivityContext);
    }

    public ActivityRouterHelper(Router router, Converter<RoutingContext, ActivityContext> defaultContextConverter) {
        this.router = router;
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

    private static ActivityContext convertRoutingContextToActivityContext(RoutingContext routingContext) {
        ActivityContext activityContext = new ActivityContext();
        activityContext.setParams(routingContext.getParams());
        activityContext.nodeProperty().addListener(new ChangeListener<GuiNode>() {
            @Override
            public void changed(ObservableValue<? extends GuiNode> observable, GuiNode oldValue, GuiNode newValue) {
                observable.removeListener(this);
                GuiToolkit.get().getApplicationWindow().nodeProperty().bind(observable);
            }
        });
        return activityContext;
    }

}
