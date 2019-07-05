package mongoose.backend.operations.roomsgraphic;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.services.update.UpdateResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

final class ToggleResourceConfigurationOnlineOfflineExecutor {

    static Future<Batch<UpdateResult>> executeRequest(ToggleResourceConfigurationOnlineOfflineRequest rq) {
        return execute(rq.getResourceConfiguration());
    }

    private static Future<Batch<UpdateResult>> execute(Entity resourceConfiguration) {
        UpdateStore updateStore = UpdateStore.create(resourceConfiguration.getStore().getDataSourceModel());
        Entity entity = updateStore.updateEntity(resourceConfiguration);
        entity.setFieldValue("online", !resourceConfiguration.getBooleanFieldValue("online"));
        return updateStore.executeUpdate();
    }
}
