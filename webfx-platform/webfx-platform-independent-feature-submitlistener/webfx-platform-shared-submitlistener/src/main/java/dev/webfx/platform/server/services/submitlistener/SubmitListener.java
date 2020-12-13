package dev.webfx.platform.server.services.submitlistener;

import dev.webfx.platform.shared.services.submit.SubmitArgument;

/**
 * @author Bruno Salmon
 */
public interface SubmitListener {

    void onSuccessfulSubmit(SubmitArgument... submitArguments);

}
