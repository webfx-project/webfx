package mongoose.activities.shared.auth;

import javafx.scene.Node;
import mongoose.activities.shared.book.event.shared.LoginPanel;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public class LoginViewActivity extends ViewActivityImpl {

    @Override
    public Node buildUi() {
        return new LoginPanel(getUiRouter().getUiUser(), getI18n(), getUiRouter().getAuthService()).getNode();
    }
}
