package mongoose.i18n;

import naga.framework.operation.i18n.ChangeLanguageRequest;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public final class FrenchLanguageRequest extends ChangeLanguageRequest {

    public FrenchLanguageRequest(I18n i18n) {
        super("fr", i18n);
    }
}
