package webfx.platform.client.jobs.querypush;

import webfx.platform.client.services.querypush.QueryPushClientService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.bus.Registration;
import webfx.platform.shared.services.querypush.spi.impl.LocalOrRemoteQueryPushServiceProvider;

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
