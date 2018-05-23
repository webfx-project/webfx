package naga.framework.operations.i18n;

import naga.framework.services.i18n.spi.I18nProvider;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
class ChangeLanguageExecutor {

    static Future<Void> executeRequest(ChangeLanguageRequest rq) {
        return execute(rq.i18n, rq.language);
    }

    private static Future<Void> execute(I18nProvider i18n, Object language) {
        i18n.setLanguage(language);
        return Future.succeededFuture();
    }
}
