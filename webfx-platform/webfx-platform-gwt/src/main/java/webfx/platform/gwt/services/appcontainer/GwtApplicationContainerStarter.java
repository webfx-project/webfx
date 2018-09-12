package webfx.platform.gwt.services.appcontainer;

import com.google.gwt.core.client.EntryPoint;

/**
 * @author Bruno Salmon
 */
public class GwtApplicationContainerStarter implements EntryPoint {

    @Override
    public void onModuleLoad() {
        new GwtApplicationContainerProvider();
    }
}
