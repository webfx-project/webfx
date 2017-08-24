package mongoose.activities.shared.auth;

import javafx.scene.Node;
import javafx.scene.text.Text;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public class UnauthorizedViewActivity extends ViewActivityImpl {

    @Override
    public Node buildUi() {
        return new Text("Sorry, you are not authorized to access this page");
    }
}
