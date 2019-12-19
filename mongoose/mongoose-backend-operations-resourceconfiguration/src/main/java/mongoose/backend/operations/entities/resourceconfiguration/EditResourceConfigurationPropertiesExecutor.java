package mongoose.backend.operations.entities.resourceconfiguration;

import javafx.scene.layout.Pane;
import webfx.framework.client.ui.controls.entity.sheet.EntityPropertiesSheet;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.Future;

final class EditResourceConfigurationPropertiesExecutor {

    static Future<Void> executeRequest(EditResourceConfigurationPropertiesRequest rq) {
        return execute(rq.getResourceConfiguration(), rq.getParentContainer());
    }

    private static Future<Void> execute(Entity resourceConfiguration, Pane parentContainer) {
        EntityPropertiesSheet.editEntity(resourceConfiguration, "name,online,max,comment", parentContainer);
        return Future.succeededFuture();
    }
}
