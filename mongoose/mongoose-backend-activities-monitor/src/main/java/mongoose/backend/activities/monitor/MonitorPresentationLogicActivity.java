package mongoose.backend.activities.monitor;

import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
final class MonitorPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<MonitorPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

    MonitorPresentationLogicActivity() {
        super(MonitorPresentationModel::new);
    }

    @Override
    protected void startLogic(MonitorPresentationModel pm) {
        createReactiveVisualFilter("{class: 'Metrics', orderBy: 'date desc', limit: '500'}")
                .setEntityColumns("['0 + id','memoryUsed','memoryTotal']")
                .visualizeResultInto(pm.memoryVisualResultProperty())
                .nextDisplay()
                .setEntityColumns("['0 + id','systemLoadAverage','processCpuLoad']")
                //.combine("{columns: '0 + id,systemLoadAverage,processCpuLoad'}")
                .visualizeResultInto(pm.cpuVisualResultProperty())
                .setPush(true)
                .start();
    }
}
