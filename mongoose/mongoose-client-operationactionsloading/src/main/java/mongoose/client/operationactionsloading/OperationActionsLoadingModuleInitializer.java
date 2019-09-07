package mongoose.client.operationactionsloading;

import mongoose.client.icons.MongooseIcons;
import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.operation.action.OperationActionRegistry;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionFactoryMixin;
import webfx.framework.client.ui.uirouter.uisession.UiSession;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public class OperationActionsLoadingModuleInitializer implements ApplicationModuleInitializer,
        OperationActionFactoryMixin,
        ActionFactoryMixin {

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
                    String iconUrl = getActionIconUrl(operationCode);
                    Action action = isPublic ? newAction(i18nCode, iconUrl) : newAuthAction(i18nCode, iconUrl, registry.authorizedOperationActionProperty(operationCode, UiSession.get().userPrincipalProperty(), UiSession.get()::isAuthorized));
                    registry.registerOperationAction(operationCode, action);
                }
            }
        });
    }

    // Temporary hardcoded icons
    private static String getActionIconUrl(String operationCode) {
        switch (operationCode) {
            case "ChangeLanguageToEnglish": return MongooseIcons.lang_englishIcon16Url;
            case "ChangeLanguageToFrench": return MongooseIcons.lang_frenchIcon16Url;
        }
        return null;
    }
}
