package mongoose.activities.backend.tester.testset;

import mongoose.activities.backend.tester.drive.Drive;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.TextField;
import naga.toolkit.spi.nodes.layouts.VBox;

/**
 * @author Jean-Pierre Alonso.
 */
public class TestSetActivity extends PresentationActivity<TestSetViewModel, TestSetPresentationModel> {

    public TestSetActivity() {
        super(TestSetPresentationModel::new);
    }

    protected TestSetViewModel buildView(Toolkit toolkit) {
        // TextFields
        TextField<String> testName = toolkit.createTextField();
        TextField<String> testComment = toolkit.createTextField();
        Button saveTest = toolkit.createButton();
        testName.setPlaceholder("Test name");
        testComment.setPlaceholder("Comments");
        saveTest.setText("Save Test");
        testName.requestFocus();

        // Arranging in a box
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(testName, testComment);


        return new TestSetViewModel(toolkit.createVPage()
                .setHeader(vBox)
                .setFooter(saveTest),
                testName,
                testComment,
                saveTest);
    }

    protected void bindViewModelWithPresentationModel(TestSetViewModel vm, TestSetPresentationModel pm) {
        // Test description
        pm.testNameProperty().bind(vm.getTestName().textProperty());
        pm.testCommentProperty().bind(vm.getTestComment().textProperty());
        vm.getSaveTest().actionEventObservable().subscribe(actionEvent -> {
            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            getHistory().goBack();
        });
    }

    protected void bindPresentationModelWithLogic(TestSetPresentationModel pm) {

    }
}
