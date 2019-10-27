package mongoose.backend.activities.monitor;

import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
final class MonitorPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<MonitorPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    MonitorPresentationLogicActivity() {
        super(MonitorPresentationModel::new);
    }

    @Override
    protected void startLogic(MonitorPresentationModel pm) {
        createReactiveExpressionFilter("{class: 'Metrics', orderBy: 'date desc', limit: '500'}")
                .setExpressionColumns("['0 + id','memoryUsed','memoryTotal']")
                .visualizeResultInto(pm.memoryVisualResultProperty())
                .nextDisplay()
                .setExpressionColumns("['0 + id','systemLoadAverage','processCpuLoad']")
                //.combine("{columns: '0 + id,systemLoadAverage,processCpuLoad'}")
                .visualizeResultInto(pm.cpuVisualResultProperty())
                //.setAutoRefresh(true) // not necessary now with the server push notification
                .start();
    }
}
