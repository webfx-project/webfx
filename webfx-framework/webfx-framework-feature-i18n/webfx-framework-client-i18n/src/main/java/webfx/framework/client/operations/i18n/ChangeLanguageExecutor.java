package webfx.framework.client.operations.i18n;

import webfx.framework.client.services.i18n.I18n;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class ChangeLanguageExecutor {

    static Future<Void> executeRequest(ChangeLanguageRequest rq) {
        return execute(rq.getLanguage());
    }

    private static Future<Void> execute(Object language) {
        I18n.setLanguage(language);
        return Future.succeededFuture();
    }
}
