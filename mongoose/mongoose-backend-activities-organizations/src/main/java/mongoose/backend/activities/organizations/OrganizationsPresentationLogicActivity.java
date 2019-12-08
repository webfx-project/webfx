package mongoose.backend.activities.organizations;

import mongoose.backend.operations.routes.events.RouteToEventsRequest;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.platform.shared.util.function.Factory;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class OrganizationsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<OrganizationsPresentationModel> {

    OrganizationsPresentationLogicActivity() {
        this(OrganizationsPresentationModel::new);
    }

    private OrganizationsPresentationLogicActivity(Factory<OrganizationsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void startLogic(OrganizationsPresentationModel pm) {
        ReactiveVisualMapper.createPushReactiveChain(this)
                .always("{class: 'Organization', alias: 'o', where: '!closed and name!=`ISC`', orderBy: 'name'}")
                // Search box condition
                .ifTrimNotEmpty(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                .ifTrue(pm.withEventsProperty(), "{where: 'exists(select Event where organization=o)', orderBy: 'id'}")
                // Limit condition
                .ifPositive(pm.limitProperty(), l -> limit("?", l))
                .setEntityColumns("[" +
                        "{label: 'Centre', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setVisualSelectionProperty(pm.genericVisualSelectionProperty())
                .setSelectedEntityHandler(organization -> new RouteToEventsRequest(organization, getHistory()).execute())
                .start();
    }
}
