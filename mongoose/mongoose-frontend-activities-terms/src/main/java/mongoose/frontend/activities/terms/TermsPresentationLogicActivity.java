package mongoose.frontend.activities.terms;

import mongoose.client.activity.bookingprocess.BookingProcessPresentationLogicActivity;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.services.i18n.I18n;

import static webfx.framework.shared.orm.dql.DqlStatement.parse;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class TermsPresentationLogicActivity
        extends BookingProcessPresentationLogicActivity<TermsPresentationModel> {

    TermsPresentationLogicActivity() {
        super(TermsPresentationModel::new);
    }

    @Override
    protected void startLogic(TermsPresentationModel pm) {
        ReactiveVisualMapper.createReactiveChain(this)
                .always("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), id -> where("event=?", id))
                .always(I18n.languageProperty(), lang -> parse("{columns: '[`html(" + lang + ")`]'}"))
                .visualizeResultInto(pm.termsLetterVisualResultProperty())
                .start();
    }
}
