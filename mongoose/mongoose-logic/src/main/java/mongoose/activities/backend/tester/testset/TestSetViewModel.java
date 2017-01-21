package mongoose.activities.backend.tester.testset;

import naga.framework.ui.presentation.ViewModelBase;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Jean-Pierre Alonso.
 */
class TestSetViewModel extends ViewModelBase {
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
