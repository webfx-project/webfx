package naga.framework.operations.i18n;

import naga.framework.operation.HasOperationExecutor;
import naga.framework.services.i18n.spi.I18nProvider;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ChangeLanguageRequest implements HasOperationExecutor<ChangeLanguageRequest, Void> {

    final Object language;
    final I18nProvider i18n;

    public ChangeLanguageRequest(Object language, I18nProvider i18n) {
        this.language = language;
        this.i18n = i18n;
    }

    @Override
    public AsyncFunction<ChangeLanguageRequest, Void> getOperationExecutor() {
        return ChangeLanguageExecutor::executeRequest;
    }
}
