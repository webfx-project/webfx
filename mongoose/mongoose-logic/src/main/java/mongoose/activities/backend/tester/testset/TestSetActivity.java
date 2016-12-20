package mongoose.activities.backend.tester.testset;

import mongoose.activities.backend.tester.drive.Drive;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.VBox;

/**
 * @author Jean-Pierre Alonso.
 */
public class TestSetActivity extends PresentationActivity<TestSetViewModel, TestSetPresentationModel> {

    public TestSetActivity() {
        super(TestSetPresentationModel::new);
    }

    protected TestSetViewModel buildView() {
        // TextFields
        TextField testName = new TextField();
        TextField testComment = new TextField();
        Button saveTest = new Button();
        testName.setPromptText("Test name");
        testComment.setPromptText("Comments");
        saveTest.setText("Save Test");
        //testName.requestFocus();

        return new TestSetViewModel(new BorderPane(null, new VBox(testName, testComment), null, saveTest, null), testName, testComment, saveTest);
    }

    protected void bindViewModelWithPresentationModel(TestSetViewModel vm, TestSetPresentationModel pm) {
        // Test description
        pm.testNameProperty().bind(vm.getTestName().textProperty());
        pm.testCommentProperty().bind(vm.getTestComment().textProperty());
        vm.getSaveTest().setOnMouseClicked(e -> {
            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            getHistory().goBack();
        });
    }

    protected void bindPresentationModelWithLogic(TestSetPresentationModel pm) {
    }
}
