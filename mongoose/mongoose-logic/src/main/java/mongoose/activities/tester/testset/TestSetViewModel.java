package mongoose.activities.tester.testset;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.TextField;

/**
 * @author Jean-Pierre Alonso.
 */
public class TestSetViewModel implements ViewModel {
    private final GuiNode contentNode;
    private final TextField testName;
    private final TextField testComment;
    private final Button saveTest;

    public TestSetViewModel(GuiNode contentNode,
                           TextField testName,
                           TextField testComment,
                           Button saveTest) {
        this.contentNode = contentNode;
        this.testName = testName;
        this.testComment = testComment;
        this.saveTest = saveTest;
    }


    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public TextField getTestName() {
        return testName;
    }

    public TextField getTestComment() {
        return testComment;
    }

    public Button getSaveTest() { return saveTest; }
}
