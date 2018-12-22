package webfx.framework.client.activity.impl.combinations.viewapplication;

import webfx.framework.client.activity.impl.combinations.viewapplication.impl.ViewApplicationContextFinal;
import webfx.framework.client.activity.impl.elementals.application.ApplicationContext;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewApplicationContext
        <THIS extends ViewApplicationContext<THIS>>

        extends ViewActivityContext<THIS>,
        ApplicationContext<THIS> {

    static ViewApplicationContextFinal create() {
        return new ViewApplicationContextFinal(ViewActivityContext::create);
    }

    static <C extends ViewApplicationContext<C>> C getViewApplicationContext() {
        return (C) ApplicationContext.get();
    }

}
