package webfx.platform.shared.services.update;

import webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;

/**
 * @author Bruno Salmon
 */
public final class ExecuteUpdateBusCallEndpoint extends AsyncFunctionBusCallEndpoint<UpdateArgument, UpdateResult> {

    public ExecuteUpdateBusCallEndpoint() {
        super(UpdateService.UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
    }
}
