package mongoose.activities.backend.event.letters;

import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public class LettersPresentationLogicActivity extends DomainPresentationLogicActivityImpl<LettersPresentationModel> {

    public LettersPresentationLogicActivity() {
        super(LettersPresentationModel::new);
    }

    protected void initializePresentationModel(LettersPresentationModel pm) {
        pm.setEventId(getParameter("eventId"));
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
                .start();
    }
}
