package naga.framework.activity.combinations.viewapplication;

import javafx.beans.property.Property;
import naga.framework.activity.view.ViewActivityContext;
import naga.framework.activity.combinations.viewapplication.impl.ViewApplicationContextFinal;
import naga.framework.activity.application.ApplicationContext;

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
