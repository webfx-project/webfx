package mongoose.activities.backend.event.letters;

import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;

/**
 * @author Bruno Salmon
 */
public class LettersPresentationLogicActivity extends EventDependentPresentationLogicActivity<LettersPresentationModel> {

    public LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    @Override
    protected void startLogic(LettersPresentationModel pm) {
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
                    if (letter != null)
                        getHistory().push("/letter/" + letter.getId().getPrimaryKey() + "/edit");
                })
                .start()
        ;
    }
}
