package mongoose.backend.activities.saveloadtest;

import mongoose.backend.activities.loadtester.drive.Drive;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;

/**
 * @author Bruno Salmon
 */
class SaveLoadTestPresentationLogicActivity extends MongooseDomainPresentationLogicActivityBase<SaveLoadTestPresentationModel> {

    SaveLoadTestPresentationLogicActivity() {
        super(SaveLoadTestPresentationModel::new);
    }

    @Override
    protected void startLogic(SaveLoadTestPresentationModel pm) {
        pm.setOnSaveTest(e -> {
            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            getHistory().goBack();
        });
    }
}
