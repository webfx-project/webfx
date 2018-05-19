package mongoose.activities.backend.monitor;

import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<MonitorPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    MonitorPresentationLogicActivity() {
        super(MonitorPresentationModel::new);
    }

    @Override
    protected void startLogic(MonitorPresentationModel pm) {
        createReactiveExpressionFilter("{class: 'Metrics', orderBy: 'date desc', limit: '500'}")
                .setExpressionColumns("['0 + id','memoryUsed','memoryTotal']")
                .displayResultInto(pm.memoryDisplayResultProperty())
                .nextDisplay()
                .setExpressionColumns("['0 + id','systemLoadAverage','processCpuLoad']")
                //.combine("{columns: '0 + id,systemLoadAverage,processCpuLoad'}")
                .displayResultInto(pm.cpuDisplayResultProperty())
                //.setAutoRefresh(true) // not necessary now with the server push notification
                .start();
    }
}
