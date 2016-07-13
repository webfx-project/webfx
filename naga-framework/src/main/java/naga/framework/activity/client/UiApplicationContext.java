package naga.framework.activity.client;

import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface UiApplicationContext<C extends UiApplicationContext<C>> extends UiActivityContext<C>, ApplicationContext<C> {

    static UiApplicationContext create(String[] mainArgs) {
        return new UiApplicationContextImpl(mainArgs, UiActivityContext::create);
    }

}
