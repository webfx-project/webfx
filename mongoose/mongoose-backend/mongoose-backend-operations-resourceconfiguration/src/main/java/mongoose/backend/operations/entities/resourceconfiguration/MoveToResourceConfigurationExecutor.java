package mongoose.backend.operations.entities.resourceconfiguration;

import mongoose.shared.entities.DocumentLine;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.util.async.Future;

final class MoveToResourceConfigurationExecutor {

    static Future<Void> executeRequest(MoveToResourceConfigurationRequest rq) {
        return execute(rq.getResourceConfiguration(), rq.getDocumentLinePrimaryKeys());
    }

    private static Future<Void> execute(Entity resourceConfiguration, Object[] documentLinePrimaryKeys) {
        Future<Void> future = Future.future();
        UpdateStore updateStore = UpdateStore.create(resourceConfiguration.getStore().getDataSourceModel());
        for (Object primaryKey : documentLinePrimaryKeys) {
            DocumentLine documentLine = updateStore.getOrCreateEntity(DocumentLine.class, primaryKey);
            updateStore.updateEntity(documentLine).setForeignField("resourceConfiguration", resourceConfiguration);
        }
        // Commented as now automatically set by the Dql submit interceptor TODO Remove this comment once the feature is completed
        //updateStore.setSubmitScope(AggregateScope.builder().addAggregate("ResourceConfiguration", resourceConfiguration.getPrimaryKey()).build());
        updateStore.submitChanges().setHandler(ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
                ar.cause().printStackTrace();
            } else
                future.complete();
        });
        return future;
    }
}
