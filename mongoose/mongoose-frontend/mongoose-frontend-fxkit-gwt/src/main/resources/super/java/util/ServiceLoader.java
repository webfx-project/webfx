package java.util;

import java.util.Iterator;
import java.util.logging.Logger;
import webfx.platforms.core.util.function.Factory;

public class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        switch (serviceClass.getName()) {
            // Single SPI providers
            case "webfx.fxkits.core.mapper.spi.FxKitMapperProvider": return new ServiceLoader<S>(webfx.fxkit.gwt.mapper.html.GwtFxKitHtmlMapperProvider::new);
            case "webfx.fxkits.core.launcher.spi.FxKitLauncherProvider": return new ServiceLoader<S>(webfx.fxkit.gwt.launcher.GwtFxKitLauncherProvider::new);
            case "webfx.platforms.core.services.resource.spi.ResourceServiceProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl::new);
            case "webfx.platforms.core.services.push.client.spi.PushClientServiceProvider": return new ServiceLoader<S>(webfx.platforms.core.services.push.client.spi.impl.PushClientServiceProviderImpl::new);
            case "webfx.platforms.core.services.query.spi.QueryServiceProvider": return new ServiceLoader<S>(webfx.platforms.core.services.query.spi.impl.LocalOrRemoteQueryServiceProviderImpl::new);
            case "webfx.platforms.core.services.update.spi.UpdateServiceProvider": return new ServiceLoader<S>(webfx.platforms.core.services.update.spi.impl.LocalOrRemoteUpdateServiceProviderImpl::new);
            case "webfx.framework.services.i18n.spi.I18nProvider": return new ServiceLoader<S>(mongooses.core.sharedends.services.i18n.MongooseI18nProvider::new);
            case "webfx.framework.services.authn.spi.AuthenticationServiceProvider": return new ServiceLoader<S>(mongooses.core.sharedends.services.authn.MongooseAuthenticationServiceProviderImpl::new);
            case "webfx.framework.services.authz.spi.AuthorizationServiceProvider": return new ServiceLoader<S>(mongooses.core.sharedends.services.authz.MongooseAuthorizationServiceProviderImpl::new);
            case "webfx.platforms.core.services.querypush.spi.QueryPushServiceProvider": return new ServiceLoader<S>(webfx.platforms.core.services.querypush.spi.remote.RemoteQueryPushServiceProviderImpl::new);
            case "webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.appcontainer.GwtApplicationContainerProvider::new);
            case "webfx.platforms.core.services.bus.spi.BusServiceProvider": return new ServiceLoader<S>(webfx.platforms.web.services.clientbus.WebClientBusServiceProvider::new);
            case "webfx.platforms.core.services.scheduler.spi.SchedulerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProvider::new);
            case "webfx.platforms.core.services.windowlocation.spi.WindowLocationProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.windowlocation.GwtWindowLocationProvider::new);
            case "webfx.platforms.core.services.json.spi.JsonProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.json.GwtJsonObject::create);
            case "webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProvider::new);
            case "webfx.platforms.core.services.log.spi.LoggerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.log.GwtLoggerProviderImpl::new);
            case "webfx.platforms.core.services.shutdown.spi.ShutdownProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.shutdown.GwtShutdownProviderImpl::new);
            case "webfx.platforms.web.services.windowhistory.JsWindowHistory": return new ServiceLoader<S>(webfx.platform.gwt.services.windowhistory.GwtJsWindowHistory::new);
            case "webfx.platforms.core.services.windowhistory.spi.WindowHistoryProvider": return new ServiceLoader<S>(webfx.platforms.web.services.windowhistory.WebWindowHistoryProvider::new);
            case "webfx.platforms.core.services.storage.spi.LocalStorageProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtLocalStorageProviderImpl::new);
            case "webfx.platforms.core.services.storage.spi.SessionStorageProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtSessionStorageProviderImpl::new);
            case "webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.websocket.GwtWebSocketServiceProviderImpl::new);
            // Multiple SPI providers
            case "webfx.platforms.core.services.buscall.spi.BusCallEndpoint": return new ServiceLoader<S>(webfx.platforms.core.services.query.ExecuteQueryBusCallEndpoint::new, webfx.platforms.core.services.query.ExecuteQueryBatchBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBatchBusCallEndpoint::new, webfx.platforms.core.services.querypush.ExecuteQueryPushBusCallEndpoint::new);
            case "webfx.framework.operations.i18n.ChangeLanguageRequestEmitter": return new ServiceLoader<S>(mongooses.core.sharedends.operations.i18n.ChangeLanguageToEnglishRequest.ProvidedEmitter::new, mongooses.core.sharedends.operations.i18n.ChangeLanguageToFrenchRequest.ProvidedEmitter::new);
            case "webfx.framework.ui.uirouter.UiRoute": return new ServiceLoader<S>(mongooses.core.activities.sharedends.authn.LoginRouting.ProvidedUiRoute::new, mongooses.core.sharedends.actvities.authz.UnauthorizedRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.startbooking.StartBookingRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.terms.TermsRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.program.ProgramRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.fees.FeesRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.options.OptionsRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.person.PersonRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.summary.SummaryRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.cart.CartRouting.ProvidedUiRoute::new, mongooses.core.frontend.activities.payment.PaymentRouting.ProvidedUiRoute::new);
            case "webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer": return new ServiceLoader<S>(webfx.fxkits.core.launcher.FxKitLauncherModuleInitializer::new, mongooses.core.frontend.start.MongooseFrontendApplicationModuleInitializer::new, webfx.platforms.core.services.buscall.BusCallModuleInitializer::new, webfx.platforms.core.services.appcontainer.spi.impl.ApplicationJobsInitializer::new, webfx.platforms.core.services.serial.SerialCodecModuleInitializer::new, webfx.platform.gwt.services.resource.GwtResourceModuleInitializer::new);
            case "webfx.platforms.core.services.appcontainer.spi.ApplicationJob": return new ServiceLoader<S>(mongooses.core.sharedends.jobs.sessionrecorder.ProvidedClientSessionRecorderJob::new);
            case "webfx.platforms.core.services.serial.spi.SerialCodec": return new ServiceLoader<S>(webfx.platforms.core.services.query.QueryArgument.ProvidedSerialCodec::new, webfx.platforms.core.services.query.QueryResult.ProvidedSerialCodec::new, webfx.platforms.core.services.buscall.BusCallArgument.ProvidedSerialCodec::new, webfx.platforms.core.services.buscall.BusCallResult.ProvidedSerialCodec::new, webfx.platforms.core.services.buscall.SerializableAsyncResult.ProvidedSerialCodec::new, webfx.platforms.core.services.update.UpdateArgument.ProvidedSerialCodec::new, webfx.platforms.core.services.update.UpdateResult.ProvidedSerialCodec::new, webfx.platforms.core.services.update.GeneratedKeyBatchIndex.ProvidedSerialCodec::new, webfx.platforms.core.services.querypush.QueryPushArgument.ProvidedSerialCodec::new, webfx.platforms.core.services.querypush.QueryPushResult.ProvidedSerialCodec::new, webfx.platforms.core.services.querypush.diff.impl.QueryResultTranslation.ProvidedSerialCodec::new, webfx.platforms.core.services.serial.spi.impl.ProvidedBatchSerialCodec::new);
            case "webfx.platform.gwt.services.resource.GwtResourceBundle": return new ServiceLoader<S>(mongooses.web.activities.sharedends.MongooseSharedEndsWebBundle.ResourceBundle::new);
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