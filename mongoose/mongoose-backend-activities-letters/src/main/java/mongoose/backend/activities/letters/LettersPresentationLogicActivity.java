package mongoose.backend.activities.letters;

import mongoose.client.activity.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.backend.operations.routes.letter.RouteToLetterRequest;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
final class LettersPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<LettersPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

    LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    @Override
    protected void startLogic(LettersPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveVisualFilter("{class: 'Letter', where: 'active', orderBy: 'id'}")
            // Condition
            .combineIfNotNull(pm.eventIdProperty(), eventId -> "{where: 'event=" + eventId + "'}")
            // Search box condition
            .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
            // Limit condition
            .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
            .setEntityColumns("[" +
                    "'name'," +
                    "'type'" +
                    "]")
            .applyDomainModelRowStyle()
            .visualizeResultInto(pm.genericVisualResultProperty())
            .setSelectedEntityHandler(pm.genericVisualSelectionProperty(), letter -> new RouteToLetterRequest(letter, getHistory()).execute() )
            .start()
        ;
    }
}
