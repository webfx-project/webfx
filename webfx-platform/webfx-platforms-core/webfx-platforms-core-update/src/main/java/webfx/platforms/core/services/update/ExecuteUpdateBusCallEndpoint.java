package webfx.platforms.core.services.update;

import webfx.platforms.core.services.buscall.spi.AsyncFunctionBusCallEndpoint;

/**
 * @author Bruno Salmon
 */
public class ExecuteUpdateBusCallEndpoint extends AsyncFunctionBusCallEndpoint<UpdateArgument, UpdateResult> {

    public ExecuteUpdateBusCallEndpoint() {
        super(UpdateService.UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
    }
}
