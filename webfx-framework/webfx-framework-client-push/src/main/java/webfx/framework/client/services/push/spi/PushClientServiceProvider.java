package webfx.framework.client.services.push.spi;

import webfx.platform.shared.services.bus.Registration;

/**
 * @author Bruno Salmon
 */
public interface PushClientServiceProvider {

    Registration listenServerPushCalls(Object pushClientId);

}
