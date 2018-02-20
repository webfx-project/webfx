package mongoose.activities.backend.tester;

import mongoose.activities.backend.tester.drive.Drive;
import mongoose.activities.backend.tester.drive.model.ConnectionChartGenerator;
import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;

/**
 * @author Bruno Salmon
 */
class TesterPresentationLogicActivity extends MongooseDomainPresentationLogicActivityBase<TesterPresentationModel> {

    public TesterPresentationLogicActivity() {
        super(TesterPresentationModel::new);
    }

    @Override
    protected void startLogic(TesterPresentationModel pm) {
        // Drive
        Drive.getInstance().start(true);
        Drive.getInstance().requestedConnectionCountProperty().bind(pm.requestedConnectionsProperty());
        pm.startedConnectionsProperty().bind(Drive.getInstance().startedConnectionCountProperty());

        ConnectionChartGenerator connectionChartGenerator = new ConnectionChartGenerator();
        connectionChartGenerator.start();
        pm.chartDisplayResultSetProperty().bind(connectionChartGenerator.connectionListProperty());

        pm.setOnSaveTest(e -> {
            getHistory().push("/saveTest");
            connectionChartGenerator.reset();
        });
    }
}
