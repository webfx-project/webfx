package webfx.platform.server.services.updatelistener;

import webfx.platform.shared.services.update.UpdateArgument;

/**
 * @author Bruno Salmon
 */
public interface UpdateListener {

    void onSuccessfulUpdate(UpdateArgument updateArgument);

}
