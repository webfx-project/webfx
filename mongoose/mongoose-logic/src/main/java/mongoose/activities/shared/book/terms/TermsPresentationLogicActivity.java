package mongoose.activities.shared.book.terms;

import mongoose.activities.shared.book.shared.BookingProcessPresentationLogicActivity;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
class TermsPresentationLogicActivity
        extends BookingProcessPresentationLogicActivity<TermsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    TermsPresentationLogicActivity() {
        super(TermsPresentationModel::new);
    }

    @Override
    protected void startLogic(TermsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .combine(pm.eventIdProperty(), e -> "{where: 'event=" + e + "'}")
                .combine(getI18n().languageProperty(), lang -> "{columns: '[`html(" + lang + ")`]'}")
                .displayResultInto(pm.termsLetterDisplayResultProperty())
                .start();
    }
}
