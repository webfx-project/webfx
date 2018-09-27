package mongooses.core.backend.activities.saveloadtest;

import mongooses.core.backend.activities.loadtester.drive.Drive;
import mongooses.core.sharedends.activities.generic.MongooseDomainPresentationLogicActivityBase;

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
