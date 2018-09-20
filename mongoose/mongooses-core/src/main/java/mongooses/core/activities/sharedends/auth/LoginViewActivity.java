package mongooses.core.activities.sharedends.auth;

import javafx.scene.Node;
import mongooses.core.activities.sharedends.book.shared.LoginPanel;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;

/**
 * @author Bruno Salmon
 */
final class LoginViewActivity extends ViewDomainActivityBase {

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
