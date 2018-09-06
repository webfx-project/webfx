package mongooses.loadtester.activities.saveloadtest;

import mongooses.loadtester.activities.loadtester.drive.Drive;
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
