package mongoose.activities.backend.tester.testset;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.TextField;

/**
 * @author Jean-Pierre Alonso.
 */
class TestSetViewModel extends AbstractViewModel {
    private final TextField testName;
    private final TextField testComment;
    private final Button saveTest;

    public TestSetViewModel(GuiNode contentNode,
                           TextField testName,
                           TextField testComment,
                           Button saveTest) {
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
