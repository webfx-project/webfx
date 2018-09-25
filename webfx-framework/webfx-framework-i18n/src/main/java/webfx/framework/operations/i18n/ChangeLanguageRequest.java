package webfx.framework.operations.i18n;

import webfx.framework.operation.HasOperationExecutor;
import webfx.platforms.core.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ChangeLanguageRequest implements HasOperationExecutor<ChangeLanguageRequest, Void> {

    private final Object language;

    public ChangeLanguageRequest(Object language) {
        this.language = language;
    }

    public Object getLanguage() {
        return language;
    }

    @Override
    public AsyncFunction<ChangeLanguageRequest, Void> getOperationExecutor() {
        return ChangeLanguageExecutor::executeRequest;
    }
}
