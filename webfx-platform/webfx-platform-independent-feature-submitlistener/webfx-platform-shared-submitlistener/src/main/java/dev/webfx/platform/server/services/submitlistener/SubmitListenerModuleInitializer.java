package dev.webfx.platform.server.services.submitlistener;

import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class SubmitListenerModuleInitializer implements ApplicationModuleInitializer {

    private List<SubmitListener> providedListener;

    @Override
    public String getModuleName() {
        return "webfx-platform-shared-submitlistener";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        providedListener = Collections.listOf(ServiceLoader.load(SubmitListener.class));
        providedListener.forEach(SubmitListenerService::addSubmitListener);
        Logger.log(providedListener.size() + " submit listeners found and registered:");
    }

    @Override
    public void exitModule() {
        providedListener.forEach(SubmitListenerService::removeSubmitListener);
    }
}
