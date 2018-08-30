package mongoose.activities.backend.loadtester.saveloadtest;

import mongoose.activities.backend.loadtester.drive.Drive;
import mongoose.activities.bothends.generic.MongooseDomainPresentationLogicActivityBase;

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
