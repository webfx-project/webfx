package mongoose.backend.operations.entities.resourceconfiguration;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

final class ToggleResourceConfigurationOnlineOfflineExecutor {

    static Future<Batch<SubmitResult>> executeRequest(ToggleResourceConfigurationOnlineOfflineRequest rq) {
        return execute(rq.getResourceConfiguration());
    }

    private static Future<Batch<SubmitResult>> execute(Entity resourceConfiguration) {
        UpdateStore updateStore = UpdateStore.create(resourceConfiguration.getStore().getDataSourceModel());
        Entity entity = updateStore.updateEntity(resourceConfiguration);
        entity.setFieldValue("online", !resourceConfiguration.getBooleanFieldValue("online"));
        return updateStore.submitChanges();
    }
}
