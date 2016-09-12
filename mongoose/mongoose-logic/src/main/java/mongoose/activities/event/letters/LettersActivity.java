package mongoose.activities.event.letters;

import mongoose.activities.shared.GenericTableActivity;
import mongoose.activities.shared.GenericTableEventDependentPresentationModel;
import mongoose.activities.shared.GenericTableViewModel;

/**
 * @author Bruno Salmon
 */
public class LettersActivity extends GenericTableActivity<GenericTableViewModel, GenericTableEventDependentPresentationModel> {

    protected void bindPresentationModelWithLogic(GenericTableEventDependentPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'active', orderBy: 'id'}")
                // Condition
                .combine(pm.eventIdProperty(), s -> "{where: 'event=" + s + "'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        "'name'," +
                        "'type'" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), letter -> {
                    if (letter != null) {
                    }
                });
    }
}
