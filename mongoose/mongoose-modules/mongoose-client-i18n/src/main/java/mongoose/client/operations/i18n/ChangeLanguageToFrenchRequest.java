package mongoose.client.operations.i18n;

import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.client.operations.i18n.ChangeLanguageRequest;
import webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitterImpl;

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

    public static final class ProvidedEmitter extends ChangeLanguageRequestEmitterImpl {
        public ProvidedEmitter() {
            super(ChangeLanguageToFrenchRequest::new);
        }
    }
}
