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
            case "SendLetter": return MongooseIcons.letterIcon16Url;
            case "ToggleMarkDocumentAsRead": return MongooseIcons.markAsReadIcon16Url;
            case "ToggleMarkDocumentAsWillPay": return MongooseIcons.willPayIcon16Url;
            case "ToggleFlagDocument": return MongooseIcons.flagIcon16Url;
            case "ToggleMarkDocumentPassAsReady": return MongooseIcons.passReadyIcon16Url;
            case "MarkDocumentPassAsUpdated": return MongooseIcons.passUpdatedIcon16Url;
            case "ToggleMarkDocumentAsArrived": return MongooseIcons.markAsArrivedIcon16Url;
            case "ToggleMarkDocumentAsUnchecked": return MongooseIcons.security_uncheckedIcon16Url;
            case "ToggleMarkDocumentAsUnknown": return MongooseIcons.security_unknownIcon16Url;
            case "ToggleMarkDocumentAsKnown": return MongooseIcons.security_knownIcon16Url;
            case "ToggleMarkDocumentAsVerified": return MongooseIcons.security_verifiedIcon16Url;
            case "AddNewTransfer":
            case "GetBackCancelledMultipleBookingsDeposit":
                return MongooseIcons.contraIcon16Url;
            case "ToggleMarkMultipleBooking": return MongooseIcons.multipleBookingIcon16Url;
            case "OpenBookingCart": return MongooseIcons.cartIcon16Url;
            case "OpenMail": return MongooseIcons.seedMailIcon16Url;
            case "ComposeNewMail": return MongooseIcons.sendMailIcon16Url;
            case "ChangeLanguageToEnglish": return MongooseIcons.lang_englishIcon16Url;
            case "ChangeLanguageToFrench": return MongooseIcons.lang_frenchIcon16Url;
        }
        if (operationCode.startsWith("Add"))
            return MongooseIcons.addIcon16Url;
        if (operationCode.startsWith("Edit"))
            return MongooseIcons.editIcon16Url;
        if (operationCode.startsWith("Cancel") || operationCode.startsWith("ToggleCancel"))
            return MongooseIcons.cancelIcon16Url;
        if (operationCode.startsWith("Confirm") || operationCode.startsWith("ToggleConfirm"))
            return MongooseIcons.confirmIcon16Url;
        if (operationCode.startsWith("Delete"))
            return MongooseIcons.deleteIcon16Url;
        if (operationCode.startsWith("Merge"))
            return MongooseIcons.mergeIcon16Url;
        if (operationCode.startsWith("Copy"))
            return MongooseIcons.copyIcon16Url;
        return null;
    }
}
