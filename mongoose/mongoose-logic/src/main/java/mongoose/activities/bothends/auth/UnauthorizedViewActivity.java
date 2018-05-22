package mongoose.activities.bothends.auth;

import javafx.scene.Node;
import javafx.scene.text.Text;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
class UnauthorizedViewActivity extends ViewActivityImpl {

    @Override
    public Node buildUi() {
        return new Text("Sorry, you are not authorized to access this page");
    }
}
