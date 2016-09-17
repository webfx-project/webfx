package mongoose.activities.backend.event.letters;

import mongoose.activities.shared.generic.GenericTableActivity;
import mongoose.activities.shared.generic.GenericTableEventDependentPresentationModel;
import mongoose.activities.shared.generic.GenericTableViewModel;

/**
 * @author Bruno Salmon
 */
public class LettersActivity extends GenericTableActivity<GenericTableViewModel, GenericTableEventDependentPresentationModel> {

    public LettersActivity() {
        super(GenericTableEventDependentPresentationModel::new);
    }

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
