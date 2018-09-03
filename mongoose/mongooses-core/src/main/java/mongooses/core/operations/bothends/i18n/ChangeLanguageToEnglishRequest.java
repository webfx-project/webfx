package mongooses.core.operations.bothends.i18n;

import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.i18n.ChangeLanguageRequest;

/**
 * @author Bruno Salmon
 */
public final class ChangeLanguageToEnglishRequest extends ChangeLanguageRequest implements HasOperationCode {

    private static final String OPERATION_CODE = "ChangeLanguageToEnglish";

    public ChangeLanguageToEnglishRequest() {
        super("en");
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
