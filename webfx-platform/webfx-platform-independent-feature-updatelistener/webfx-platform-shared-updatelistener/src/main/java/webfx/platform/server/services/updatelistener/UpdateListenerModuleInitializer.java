package webfx.platform.server.services.updatelistener;

import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class UpdateListenerModuleInitializer implements ApplicationModuleInitializer {

    private List<UpdateListener> providedListener;

    @Override
    public String getModuleName() {
        return "webfx-platform-shared-updatelistener";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        providedListener = Collections.listOf(ServiceLoader.load(UpdateListener.class));
        providedListener.forEach(UpdateListenerService::addUpdateListener);
        Logger.log(providedListener.size() + " update listeners found and registered:");
    }

    @Override
    public void exitModule() {
        providedListener.forEach(UpdateListenerService::removeUpdateListener);
    }
}
