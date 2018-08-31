package webfx.framework.operations.i18n;

import webfx.framework.services.i18n.I18n;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class ChangeLanguageExecutor {

    static Future<Void> executeRequest(ChangeLanguageRequest rq) {
        return execute(rq.language);
    }

    private static Future<Void> execute(Object language) {
        I18n.setLanguage(language);
        return Future.succeededFuture();
    }
}
