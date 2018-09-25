package mongooses.core.backend.activities.letters;

import mongooses.core.sharedends.activities.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongooses.core.backend.operations.letter.RouteToLetterRequest;
import webfx.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
final class LettersPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<LettersPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    @Override
    protected void startLogic(LettersPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'active', orderBy: 'id'}")
            // Condition
            .combineIfNotNull(pm.eventIdProperty(), eventId -> "{where: 'event=" + eventId + "'}")
            // Search box condition
            .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
            // Limit condition
            .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
            .setExpressionColumns("[" +
                    "'name'," +
                    "'type'" +
                    "]")
            .applyDomainModelRowStyle()
            .displayResultInto(pm.genericDisplayResultProperty())
            .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), letter -> new RouteToLetterRequest(letter, getHistory()).execute() )
            .start()
        ;
    }
}
