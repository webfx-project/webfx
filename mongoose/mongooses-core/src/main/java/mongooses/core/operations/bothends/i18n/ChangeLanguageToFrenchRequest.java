package mongooses.core.operations.bothends.i18n;

import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.i18n.ChangeLanguageRequest;

/**
 * @author Bruno Salmon
 */
public final class ChangeLanguageToFrenchRequest extends ChangeLanguageRequest implements HasOperationCode {

    private static final String OPERATION_CODE = "ChangeLanguageToFrench";

    public ChangeLanguageToFrenchRequest() {
        super("fr");
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
