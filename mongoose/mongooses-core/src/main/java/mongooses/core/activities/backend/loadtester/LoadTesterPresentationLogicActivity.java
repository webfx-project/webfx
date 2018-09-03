package mongooses.core.activities.backend.loadtester;

import mongooses.core.activities.backend.loadtester.drive.Drive;
import mongooses.core.activities.backend.loadtester.drive.model.ConnectionChartGenerator;
import mongooses.core.activities.sharedends.generic.MongooseDomainPresentationLogicActivityBase;

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
