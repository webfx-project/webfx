package naga.framework.operations.i18n;

import naga.framework.operation.HasOperationExecutor;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ChangeLanguageRequest implements HasOperationExecutor<ChangeLanguageRequest, Void> {

    final Object language;

    public ChangeLanguageRequest(Object language) {
        this.language = language;
    }

    @Override
    public AsyncFunction<ChangeLanguageRequest, Void> getOperationExecutor() {
        return ChangeLanguageExecutor::executeRequest;
    }
}
