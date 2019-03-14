package mongoose.frontend.activities.terms;

import mongoose.client.activity.bookingprocess.BookingProcessPresentationLogicActivity;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;

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
