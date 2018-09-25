package mongooses.core.frontend.activities.terms;

import mongooses.core.sharedends.activities.shared.BookingProcessPresentationLogicActivity;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
final class TermsPresentationLogicActivity
        extends BookingProcessPresentationLogicActivity<TermsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    TermsPresentationLogicActivity() {
        super(TermsPresentationModel::new);
    }

    @Override
    protected void startLogic(TermsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), id -> "{where: 'event=" + id + "'}")
                .combine(I18n.languageProperty(), lang -> "{columns: '[`html(" + lang + ")`]'}")
                .displayResultInto(pm.termsLetterDisplayResultProperty())
                .start();
    }
}
