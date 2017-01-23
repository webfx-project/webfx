package mongoose.activities.backend.tester.savetest;

import mongoose.activities.backend.tester.drive.Drive;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public class SaveTestPresentationLogicActivity extends DomainPresentationLogicActivityImpl<SaveTestPresentationModel> {

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
