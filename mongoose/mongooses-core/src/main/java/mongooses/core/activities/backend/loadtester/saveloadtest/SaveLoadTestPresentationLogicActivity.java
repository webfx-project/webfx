package mongooses.core.activities.backend.loadtester.saveloadtest;

import mongooses.core.activities.backend.loadtester.drive.Drive;
import mongooses.core.activities.sharedends.generic.MongooseDomainPresentationLogicActivityBase;

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
