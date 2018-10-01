package mongoose.client.operationactionsloading;

import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.operation.action.OperationActionFactoryMixin;
import webfx.framework.operation.action.OperationActionRegistry;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.action.ActionFactoryMixin;
import webfx.framework.ui.uirouter.uisession.UiSession;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public class OperationActionsLoadingModuleInitializer implements ApplicationModuleInitializer, OperationActionFactoryMixin, ActionFactoryMixin {

    @Override
    public String getModuleName() {
        return "mongoose-client-operationactionsloading";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        EntityStore.create(dataSourceModel).executeQuery("select operationCode,i18nCode,public from Operation").setHandler(ar -> {
            if (ar.failed())
                Logger.log("Failed loading operations", ar.cause());
            else {
                OperationActionRegistry registry = getOperationActionRegistry();
                for (Entity operation : ar.result()) {
                    String operationCode = operation.getStringFieldValue("operationCode");
                    String i18nCode = operation.getStringFieldValue("i18nCode");
                    boolean isPublic = operation.getBooleanFieldValue("public");
                    Action action = isPublic ? newAction(i18nCode) : newAuthAction(i18nCode, registry.authorizedOperationActionProperty(operationCode, UiSession.get().userPrincipalProperty(), UiSession.get()::isAuthorized));
                    registry.registerOperationAction(operationCode, action);
                }
            }
        });
    }
}
