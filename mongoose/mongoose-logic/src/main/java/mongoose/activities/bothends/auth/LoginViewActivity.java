package mongoose.activities.bothends.auth;

import javafx.scene.Node;
import mongoose.activities.bothends.book.shared.LoginPanel;
import naga.framework.activity.base.elementals.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
class LoginViewActivity extends ViewActivityImpl {

    private LoginPanel loginPanel;

    @Override
    public Node buildUi() {
        loginPanel = new LoginPanel(getUiSession());
        return loginPanel.getNode();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loginPanel != null)
            loginPanel.prepareShowing();
    }
}
