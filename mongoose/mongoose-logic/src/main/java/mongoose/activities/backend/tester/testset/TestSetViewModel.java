package mongoose.activities.backend.tester.testset;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.TextField;

/**
 * @author Jean-Pierre Alonso.
 */
class TestSetViewModel extends AbstractViewModel {
    private final TextField testName;
    private final TextField testComment;
    private final Button saveTest;

    public TestSetViewModel(Node contentNode, TextField testName, TextField testComment, Button saveTest) {
        super(contentNode);
        this.testName = testName;
        this.testComment = testComment;
        this.saveTest = saveTest;
    }

    TextField getTestName() {
        return testName;
    }

    TextField getTestComment() {
        return testComment;
    }

    Button getSaveTest() { return saveTest; }
}
