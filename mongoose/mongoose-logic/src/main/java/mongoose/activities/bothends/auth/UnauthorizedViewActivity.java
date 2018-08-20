package mongoose.activities.bothends.auth;

import javafx.scene.Node;
import javafx.scene.text.Text;
import naga.framework.activity.base.elementals.view.impl.ViewDomainActivityBase;

/**
 * @author Bruno Salmon
 */
final class UnauthorizedViewActivity extends ViewDomainActivityBase {

    @Override
    public Node buildUi() {
        return new Text("Sorry, you are not authorized to access this page");
    }
}
