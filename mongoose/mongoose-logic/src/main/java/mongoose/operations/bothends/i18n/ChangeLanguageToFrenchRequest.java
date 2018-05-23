package mongoose.operations.bothends.i18n;

import naga.framework.operation.HasOperationCode;
import naga.framework.operations.i18n.ChangeLanguageRequest;
import naga.framework.services.i18n.spi.I18nProvider;

/**
 * @author Bruno Salmon
 */
public final class ChangeLanguageToFrenchRequest extends ChangeLanguageRequest implements HasOperationCode {

    private static final String OPERATION_CODE = "FRENCH";

    public ChangeLanguageToFrenchRequest(I18nProvider i18n) {
        super("fr", i18n);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
