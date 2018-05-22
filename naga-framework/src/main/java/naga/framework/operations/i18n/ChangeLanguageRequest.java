package naga.framework.operations.i18n;

import naga.framework.operation.HasOperationExecutor;
import naga.framework.ui.i18n.I18n;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ChangeLanguageRequest implements HasOperationExecutor<ChangeLanguageRequest, Void> {

    final Object language;
    final I18n i18n;

    public ChangeLanguageRequest(Object language, I18n i18n) {
        this.language = language;
        this.i18n = i18n;
    }

    @Override
    public AsyncFunction<ChangeLanguageRequest, Void> getOperationExecutor() {
        return ChangeLanguageExecutor::executeRequest;
    }
}
