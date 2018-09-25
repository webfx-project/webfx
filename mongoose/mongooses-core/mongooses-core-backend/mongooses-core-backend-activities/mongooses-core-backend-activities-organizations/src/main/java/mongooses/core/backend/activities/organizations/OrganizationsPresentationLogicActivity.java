package mongooses.core.backend.activities.organizations;

import mongooses.core.sharedends.activities.generic.MongooseDomainPresentationLogicActivityBase;
import mongooses.core.backend.operations.events.RouteToEventsRequest;
import mongooses.core.shared.entities.Organization;
import webfx.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
final class OrganizationsPresentationLogicActivity
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
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
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
