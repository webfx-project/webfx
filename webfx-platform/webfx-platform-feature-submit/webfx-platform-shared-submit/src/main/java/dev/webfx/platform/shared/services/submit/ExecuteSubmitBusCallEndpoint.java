package dev.webfx.platform.shared.services.submit;

import dev.webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;

/**
 * @author Bruno Salmon
 */
public final class ExecuteSubmitBusCallEndpoint extends AsyncFunctionBusCallEndpoint<SubmitArgument, SubmitResult> {

    public ExecuteSubmitBusCallEndpoint() {
        super(SubmitService.SUBMIT_SERVICE_ADDRESS, SubmitService::executeSubmit);
    }
}
