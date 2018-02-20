package mongoose.activities.backend.tester.savetest;

import mongoose.activities.backend.tester.drive.Drive;
import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;

/**
 * @author Bruno Salmon
 */
class SaveTestPresentationLogicActivity extends MongooseDomainPresentationLogicActivityBase<SaveTestPresentationModel> {

    public SaveTestPresentationLogicActivity() {
        super(SaveTestPresentationModel::new);
    }

    @Override
    protected void startLogic(SaveTestPresentationModel pm) {
        pm.setOnSaveTest(e -> {
            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            getHistory().goBack();
        });
    }
}
