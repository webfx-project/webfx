package java.util;

import java.util.Iterator;
import java.util.logging.Logger;
import webfx.platform.shared.util.function.Factory;

public class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        switch (serviceClass.getName()) {
            // Single SPI providers
            case "webfx.fxkit.launcher.spi.FxKitLauncherProvider": return new ServiceLoader<S>(webfx.fxkit.gwt.launcher.GwtFxKitLauncherProvider::new);
            case "webfx.fxkit.mapper.spi.FxKitMapperProvider": return new ServiceLoader<S>(webfx.fxkit.gwt.mapper.html.GwtFxKitHtmlMapperProvider::new);
            case "webfx.platform.shared.services.resource.spi.ResourceServiceProvider": return new ServiceLoader<S>(webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceServiceProvider::new);
            case "webfx.platform.shared.services.query.spi.QueryServiceProvider": return new ServiceLoader<S>(webfx.platform.shared.services.query.spi.impl.LocalOrRemoteQueryServiceProvider::new);
            case "webfx.platform.shared.services.update.spi.UpdateServiceProvider": return new ServiceLoader<S>(webfx.platform.shared.services.update.spi.impl.LocalOrRemoteUpdateServiceProvider::new);
            case "webfx.framework.client.services.i18n.spi.I18nProvider": return new ServiceLoader<S>(mongoose.client.services.i18n.MongooseI18nProvider::new);
            case "webfx.framework.shared.services.authn.spi.AuthenticationServiceProvider": return new ServiceLoader<S>(mongoose.client.services.authn.MongooseAuthenticationServiceProvider::new);
            case "webfx.framework.shared.services.authz.spi.AuthorizationServiceProvider": return new ServiceLoader<S>(mongoose.client.services.authz.MongooseAuthorizationServiceProvider::new);
            case "webfx.platform.shared.services.appcontainer.spi.ApplicationContainerProvider": return new ServiceLoader<S>(webfx.platform.shared.services.appcontainer.spi.impl.gwt.GwtApplicationContainerProvider::new);
            case "webfx.platform.shared.services.bus.spi.BusServiceProvider": return new ServiceLoader<S>(webfx.platform.client.services.websocketbus.web.WebWebsocketBusServiceProvider::new);
            case "webfx.platform.shared.services.scheduler.spi.SchedulerProvider": return new ServiceLoader<S>(webfx.platform.client.services.uischeduler.spi.impl.gwt.GwtUiSchedulerProvider::new);
            case "webfx.platform.client.services.windowlocation.spi.WindowLocationProvider": return new ServiceLoader<S>(webfx.platform.client.services.windowlocation.spi.impl.gwt.GwtWindowLocationProvider::new);
            case "webfx.platform.shared.services.json.spi.JsonProvider": return new ServiceLoader<S>(webfx.platform.shared.services.json.spi.impl.gwt.GwtJsonObject::create);
            case "webfx.platform.client.services.uischeduler.spi.UiSchedulerProvider": return new ServiceLoader<S>(webfx.platform.client.services.uischeduler.spi.impl.gwt.GwtUiSchedulerProvider::new);
            case "webfx.platform.shared.services.log.spi.LoggerProvider": return new ServiceLoader<S>(webfx.platform.shared.services.log.spi.impl.gwt.GwtLoggerProvider::new);
            case "webfx.platform.shared.services.shutdown.spi.ShutdownProvider": return new ServiceLoader<S>(webfx.platform.shared.services.shutdown.spi.impl.gwt.GwtShutdownProvider::new);
            case "webfx.platform.client.services.windowhistory.spi.impl.web.JsWindowHistory": return new ServiceLoader<S>(webfx.platform.client.services.windowhistory.spi.impl.gwt.GwtJsWindowHistory::new);
            case "webfx.platform.client.services.windowhistory.spi.WindowHistoryProvider": return new ServiceLoader<S>(webfx.platform.client.services.windowhistory.spi.impl.web.WebWindowHistoryProvider::new);
            case "webfx.platform.client.services.storage.spi.LocalStorageProvider": return new ServiceLoader<S>(webfx.platform.client.services.storage.spi.impl.gwt.GwtLocalStorageProvider::new);
            case "webfx.platform.client.services.storage.spi.SessionStorageProvider": return new ServiceLoader<S>(webfx.platform.client.services.storage.spi.impl.gwt.GwtSessionStorageProvider::new);
            case "webfx.platform.client.services.websocket.spi.WebSocketServiceProvider": return new ServiceLoader<S>(webfx.platform.client.services.websocket.spi.impl.gwt.GwtWebSocketServiceProvider::new);
            case "webfx.framework.client.services.push.spi.PushClientServiceProvider": return new ServiceLoader<S>(webfx.framework.client.services.push.spi.impl.simple.SimplePushClientServiceProvider::new);
            case "webfx.framework.shared.services.querypush.spi.QueryPushServiceProvider": return new ServiceLoader<S>(webfx.framework.shared.services.querypush.spi.impl.LocalOrRemoteQueryPushServiceProvider::new);
            // Multiple SPI providers
            case "webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitter": return new ServiceLoader<S>(mongoose.client.operations.i18n.ChangeLanguageToEnglishRequest.ProvidedEmitter::new, mongoose.client.operations.i18n.ChangeLanguageToFrenchRequest.ProvidedEmitter::new);
            case "webfx.framework.client.ui.uirouter.UiRoute": return new ServiceLoader<S>(mongoose.client.activities.login.LoginUiRoute::new, mongoose.client.activities.unauthorized.UnauthorizedUiRoute::new, mongoose.frontend.activities.startbooking.StartBookingUiRoute::new, mongoose.frontend.activities.terms.TermsUiRoute::new, mongoose.frontend.activities.program.ProgramUiRoute::new, mongoose.frontend.activities.fees.FeesUiRoute::new, mongoose.frontend.activities.options.OptionsUiRoute::new, mongoose.frontend.activities.person.PersonUiRoute::new, mongoose.frontend.activities.summary.SummaryUiRoute::new, mongoose.frontend.activities.cart.CartUiRoute::new, mongoose.frontend.activities.payment.PaymentUiRoute::new);
            case "webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer": return new ServiceLoader<S>(webfx.fxkit.launcher.FxKitLauncherModuleInitializer::new, mongoose.frontend.start.MongooseFrontendApplicationModuleInitializer::new, mongoose.client.operationactionsloading.OperationActionsLoadingModuleInitializer::new, webfx.platform.shared.services.appcontainer.spi.impl.ApplicationJobsStarter::new, webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceModuleInitializer::new, webfx.platform.shared.services.buscall.BusCallModuleInitializer::new, webfx.platform.shared.services.serial.SerialCodecModuleInitializer::new);
            case "webfx.platform.shared.services.appcontainer.spi.ApplicationJob": return new ServiceLoader<S>(mongoose.client.jobs.sessionrecorder.ClientSessionRecorderJob::new, webfx.framework.client.jobs.querypush.QueryPushClientJob::new);
            case "webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceBundle": return new ServiceLoader<S>(mongoose.client.services.bus.conf.gwt.MongooseBusOptionsGwtBundle.ProvidedGwtResourceBundle::new, mongoose.client.services.i18n.gwt.MongooseClientI18nGwtBundle.ProvidedGwtResourceBundle::new, mongoose.client.icons.gwt.MongooseIconsGwtBundle.ProvidedGwtResourceBundle::new, mongoose.shared.domain.gwt.MongooseDomainSnapshotGwtBundle.ProvidedGwtResourceBundle::new);
            case "webfx.platform.shared.services.buscall.spi.BusCallEndpoint": return new ServiceLoader<S>(webfx.platform.shared.services.query.ExecuteQueryBusCallEndpoint::new, webfx.platform.shared.services.query.ExecuteQueryBatchBusCallEndpoint::new, webfx.platform.shared.services.update.ExecuteUpdateBusCallEndpoint::new, webfx.platform.shared.services.update.ExecuteUpdateBatchBusCallEndpoint::new, webfx.framework.shared.services.querypush.ExecuteQueryPushBusCallEndpoint::new);
            case "webfx.platform.shared.services.serial.spi.SerialCodec": return new ServiceLoader<S>(webfx.platform.shared.services.query.QueryArgument.ProvidedSerialCodec::new, webfx.platform.shared.services.query.QueryResult.ProvidedSerialCodec::new, webfx.platform.shared.services.update.UpdateArgument.ProvidedSerialCodec::new, webfx.platform.shared.services.update.UpdateResult.ProvidedSerialCodec::new, webfx.platform.shared.services.update.GeneratedKeyBatchIndex.ProvidedSerialCodec::new, webfx.platform.shared.services.buscall.BusCallArgument.ProvidedSerialCodec::new, webfx.platform.shared.services.buscall.BusCallResult.ProvidedSerialCodec::new, webfx.platform.shared.services.buscall.SerializableAsyncResult.ProvidedSerialCodec::new, webfx.platform.shared.services.serial.spi.impl.ProvidedBatchSerialCodec::new, webfx.framework.shared.services.querypush.QueryPushArgument.ProvidedSerialCodec::new, webfx.framework.shared.services.querypush.QueryPushResult.ProvidedSerialCodec::new, webfx.framework.shared.services.querypush.diff.impl.QueryResultTranslation.ProvidedSerialCodec::new);
            // SPI NOT FOUND
            default:
               Logger.getLogger(ServiceLoader.class.getName()).warning("SPI not found for " + serviceClass);
               return new ServiceLoader<S>();
        }
    }

    private final Factory[] factories;

    public ServiceLoader(Factory... factories) {
        this.factories = factories;
    }

    public Iterator<S> iterator() {
        return new Iterator<S>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < factories.length;
            }

            @Override
            public S next() {
                return (S) factories[index++].create();
            }
        };
    }
}