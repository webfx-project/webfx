package webfx.framework.activity.impl.combinations.viewapplication;

import javafx.beans.property.Property;
import webfx.framework.activity.impl.elementals.view.ViewActivityContext;
import webfx.framework.activity.impl.combinations.viewapplication.impl.ViewApplicationContextFinal;
import webfx.framework.activity.impl.elementals.application.ApplicationContext;

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
