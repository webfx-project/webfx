package mongooses.core.backend.activities.loadtester;

import mongooses.core.backend.activities.loadtester.drive.Drive;
import mongooses.core.backend.activities.loadtester.drive.model.ConnectionChartGenerator;
import mongooses.core.sharedends.activities.generic.MongooseDomainPresentationLogicActivityBase;

/**
 * @author Bruno Salmon
 */
final class LoadTesterPresentationLogicActivity extends MongooseDomainPresentationLogicActivityBase<LoadTesterPresentationModel> {

    LoadTesterPresentationLogicActivity() {
        super(LoadTesterPresentationModel::new);
    }

    @Override
    protected void startLogic(LoadTesterPresentationModel pm) {
        // Drive
        Drive.getInstance().start(true);
        Drive.getInstance().requestedConnectionCountProperty().bind(pm.requestedConnectionsProperty());
        pm.startedConnectionsProperty().bind(Drive.getInstance().startedConnectionCountProperty());

        ConnectionChartGenerator connectionChartGenerator = new ConnectionChartGenerator();
        connectionChartGenerator.start();
        pm.chartDisplayResultProperty().bind(connectionChartGenerator.connectionListProperty());

        pm.setOnSaveTest(e -> {
            getHistory().push("/saveTest");
            connectionChartGenerator.reset();
        });
    }
}
