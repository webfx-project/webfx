package webfx.platform.server.services.submitlistener;

import webfx.platform.shared.services.submit.SubmitArgument;

/**
 * @author Bruno Salmon
 */
public interface SubmitListener {

    void onSuccessfulSubmit(SubmitArgument... submitArguments);

}
