package mongoose.frontend.activities.terms;

import mongoose.client.activity.bookingprocess.BookingProcessPresentationLogicActivity;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.framework.client.services.i18n.I18n;

import static webfx.framework.client.orm.entity.filter.DqlStatement.parse;
import static webfx.framework.client.orm.entity.filter.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class TermsPresentationLogicActivity
        extends BookingProcessPresentationLogicActivity<TermsPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

    TermsPresentationLogicActivity() {
        super(TermsPresentationModel::new);
    }

    @Override
    protected void startLogic(TermsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveVisualFilter("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), id -> where("event=?", id))
                .combine(I18n.languageProperty(), lang -> parse("{columns: '[`html(" + lang + ")`]'}"))
                .visualizeResultInto(pm.termsLetterVisualResultProperty())
                .start();
    }
}
