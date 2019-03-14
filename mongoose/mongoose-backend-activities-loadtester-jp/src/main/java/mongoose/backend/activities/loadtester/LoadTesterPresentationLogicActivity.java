package mongoose.backend.activities.loadtester;

import mongoose.backend.activities.loadtester.drive.Drive;
import mongoose.backend.activities.loadtester.drive.model.ConnectionChartGenerator;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;

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
