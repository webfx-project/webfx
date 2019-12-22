package mongoose.backend.activities.saveloadtest;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import webfx.framework.client.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.util.scene.SceneUtil;

/**
 * @author Bruno Salmon
 */
class SaveLoadTestPresentationViewActivity
        extends PresentationViewActivityImpl<SaveLoadTestPresentationModel>
        implements ButtonFactoryMixin {

    private TextField testName;
    private TextField testComment;
    private Button saveTest;

    @Override
    protected void createViewNodes(SaveLoadTestPresentationModel pm) {
        testName = newTextField();
        testComment = newTextField();
        saveTest = newButton();
        testName.setPromptText("Test name");
        testComment.setPromptText("Comments");
        saveTest.setText("Save Test");

        pm.testNameProperty().bind(testName.textProperty());
        pm.testCommentProperty().bind(testComment.textProperty());
        saveTest.onActionProperty().bind(pm.onSaveTestProperty());

        SceneUtil.autoFocusIfEnabled(testName);
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(null, new VBox(testName, testComment), null, saveTest, null);
    }
}
