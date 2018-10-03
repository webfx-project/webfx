package webfx.framework.client.jobs.querypush;

import webfx.framework.client.services.querypush.QueryPushClientService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.bus.Registration;
import webfx.framework.shared.services.querypush.spi.impl.LocalOrRemoteQueryPushServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class QueryPushClientJob implements ApplicationJob {

    private Registration registration;

    @Override
    public void onStart() {
        registration = QueryPushClientService.registerQueryPushClientConsumer(LocalOrRemoteQueryPushServiceProvider::onQueryPushResultReceived);
    }

    @Override
    public void onStop() {
        if (registration != null)
            registration.unregister();
    }
}
