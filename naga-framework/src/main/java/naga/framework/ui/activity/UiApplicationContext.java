package naga.framework.ui.activity;

import naga.platform.activity.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface UiApplicationContext<C extends UiApplicationContext<C>> extends UiActivityContext<C>, ApplicationContext<C> {

    static UiApplicationContext create(String[] mainArgs) {
        return new UiApplicationContextImpl(mainArgs, UiActivityContext::create);
    }

}
