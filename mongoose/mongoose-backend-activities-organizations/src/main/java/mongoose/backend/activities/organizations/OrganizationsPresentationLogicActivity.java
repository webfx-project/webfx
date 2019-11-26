package mongoose.backend.activities.organizations;

import mongoose.backend.operations.routes.events.RouteToEventsRequest;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import mongoose.shared.entities.Organization;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.platform.shared.util.function.Factory;

import static webfx.framework.client.orm.entity.filter.DqlStatement.limit;
import static webfx.framework.client.orm.entity.filter.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class OrganizationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OrganizationsPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

    OrganizationsPresentationLogicActivity() {
        this(OrganizationsPresentationModel::new);
    }

    private OrganizationsPresentationLogicActivity(Factory<OrganizationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        this.<Organization>createReactiveVisualFilter("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'name'}")
                // Search box condition
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> limit("?", l))
                .combineIfTrue(pm.withEventsProperty(), "{where: 'exists(select Event where organization=o)', orderBy: 'id'}")
                .setEntityColumns("[" +
                        "{label: 'Centre', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setSelectedEntityHandler(pm.genericVisualSelectionProperty(), organization -> new RouteToEventsRequest(organization, getHistory()).execute() )
                .setPush(true)
                .start();
    }
}
