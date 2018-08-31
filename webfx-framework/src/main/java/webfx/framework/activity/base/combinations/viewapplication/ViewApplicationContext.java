package webfx.framework.activity.base.combinations.viewapplication;

import javafx.beans.property.Property;
import webfx.framework.activity.base.elementals.view.ViewActivityContext;
import webfx.framework.activity.base.combinations.viewapplication.impl.ViewApplicationContextFinal;
import webfx.framework.activity.base.elementals.application.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface ViewApplicationContext
        <THIS extends ViewApplicationContext<THIS>>

        extends ViewActivityContext<THIS>,
        ApplicationContext<THIS> {

    Property<Boolean> windowBoundProperty();

    default boolean isWindowBound() { return windowBoundProperty().getValue(); }

    static ViewApplicationContextFinal create(String[] mainArgs) {
        return new ViewApplicationContextFinal(mainArgs, ViewActivityContext::create);
    }

    static <C extends ViewApplicationContext<C>> C getViewApplicationContext() {
        return (C) ApplicationContext.get();
    }

}
