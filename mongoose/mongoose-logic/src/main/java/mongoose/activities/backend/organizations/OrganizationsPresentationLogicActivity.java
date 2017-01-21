package mongoose.activities.backend.organizations;

import naga.commons.util.function.Factory;
import naga.framework.activity.presentationlogic.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationLogicActivity extends DomainPresentationLogicActivityImpl<OrganizationsPresentationModel> {

    public OrganizationsPresentationLogicActivity() {
        this(OrganizationsPresentationModel::new);
    }

    public OrganizationsPresentationLogicActivity(Factory<OrganizationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Organization', alias: 'o', where: '!closed', orderBy: 'name'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .combine(pm.withEventsProperty(), "{where: 'exists(select Event where live and organization=o)', orderBy: 'id'}")
                .setExpressionColumns("[" +
                        "{label: 'Centre', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), organization -> {
                    if (organization != null)
                        getHistory().push("/organization/" + organization.getPrimaryKey() + "/events");
                }).start();
    }
}
