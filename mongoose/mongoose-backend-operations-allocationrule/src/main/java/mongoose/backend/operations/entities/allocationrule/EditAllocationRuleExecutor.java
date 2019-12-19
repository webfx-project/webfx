package mongoose.backend.operations.entities.allocationrule;

import javafx.scene.layout.Pane;
import webfx.framework.client.ui.controls.entity.sheet.EntityPropertiesSheet;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.Future;

final class EditAllocationRuleExecutor {

    static Future<Void> executeRequest(EditAllocationRuleRequest rq) {
        return execute(rq.getAllocationRule(), rq.getParentContainer());
    }

    private static Future<Void> execute(Entity allocationRule, Pane parentContainer) {
        EntityPropertiesSheet.editEntity(allocationRule, "<default>", parentContainer);
        return Future.succeededFuture();
    }
}
