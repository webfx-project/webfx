package mongoose.activities.shared.auth;

import javafx.scene.Node;
import mongoose.activities.shared.book.event.shared.LoginPanel;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
class LoginViewActivity extends ViewActivityImpl {

    private LoginPanel loginPanel;

    @Override
    public Node buildUi() {
        loginPanel = new LoginPanel(getUiSession(), getI18n(), getUiRouter().getAuthenticationService());
        return loginPanel.getNode();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loginPanel != null)
            loginPanel.prepareShowing();
    }
}
