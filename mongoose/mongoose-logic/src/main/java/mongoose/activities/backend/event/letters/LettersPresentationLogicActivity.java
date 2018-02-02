package mongoose.activities.backend.event.letters;

import mongoose.activities.backend.letter.edit.EditLetterRouting;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;

/**
 * @author Bruno Salmon
 */
class LettersPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<LettersPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    public LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    @Override
    protected void startLogic(LettersPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'active', orderBy: 'id'}")
            // Condition
            .combineIfNotNull(pm.eventIdProperty(), eventId -> "{where: 'event=" + eventId + "'}")
            // Search box condition
            .combineTrimIfNotEmpty(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
            // Limit condition
            .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
            .setExpressionColumns("[" +
                    "'name'," +
                    "'type'" +
                    "]")
            .applyDomainModelRowStyle()
            .displayResultSetInto(pm.genericDisplayResultSetProperty())
            .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), letter -> EditLetterRouting.routeUsingLetter(letter, getHistory()))
            .start()
        ;
    }
}
