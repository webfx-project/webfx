package mongoose.activities.backend.organizations;

import mongoose.activities.backend.events.RouteToEventsRequest;
import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;
import mongoose.entities.Organization;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OrganizationsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    OrganizationsPresentationLogicActivity() {
        this(OrganizationsPresentationModel::new);
    }

    private OrganizationsPresentationLogicActivity(Factory<OrganizationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        this.<Organization>createReactiveExpressionFilter("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'name'}")
                // Search box condition
                .combineTrimIfNotEmpty(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
                .combine(pm.withEventsProperty(), "{where: 'exists(select Event where organization=o)', orderBy: 'id'}")
                .setExpressionColumns("[" +
                        "{label: 'Centre', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultInto(pm.genericDisplayResultProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), organization -> new RouteToEventsRequest(organization, getHistory()).execute() )
                .start();
    }
}
