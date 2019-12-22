package mongoose.backend.activities.letters;

import mongoose.backend.operations.routes.letter.RouteToLetterRequest;
import mongoose.client.activity.eventdependent.EventDependentPresentationLogicActivity;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class LettersPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<LettersPresentationModel> {

    LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    @Override
    protected void startLogic(LettersPresentationModel pm) {
        ReactiveVisualMapper.createReactiveChain(this)
                .always("{class: 'Letter', where: 'active', orderBy: 'id'}")
                // Condition
                .ifNotNull(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                // Search box condition
                .ifTrimNotEmpty(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                // Limit condition
                .ifPositive(pm.limitProperty(), l -> limit("?", l))
                .setEntityColumns("[" +
                "'name'," +
                "'type'" +
                "]")
                .applyDomainModelRowStyle()
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setVisualSelectionProperty(pm.genericVisualSelectionProperty())
                .setSelectedEntityHandler(letter -> new RouteToLetterRequest(letter, getHistory()).execute())
                .start();
    }
}
