package webfx.framework.operations.i18n;

import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class ChangeLanguageRequestEmitterImpl implements ChangeLanguageRequestEmitter {

    private final Factory<ChangeLanguageRequest> changeLanguageRequestFactory;

    public ChangeLanguageRequestEmitterImpl(Factory<ChangeLanguageRequest> changeLanguageRequestFactory) {
        this.changeLanguageRequestFactory = changeLanguageRequestFactory;
    }

    @Override
    public ChangeLanguageRequest emitLanguageRequest() {
        return changeLanguageRequestFactory.create();
    }
}
