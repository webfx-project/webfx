package mongoose.activities.backend.operations;

import javafx.scene.Node;
import javafx.scene.text.Text;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
class OperationsActivity extends ViewActivityImpl {

    @Override
    public void onStart() {
    }

    @Override
    public Node buildUi() {
        return new Text("Hello operations");
    }
}
