package mongoose.backend.activities.users;

import javafx.scene.Node;
import mongoose.backend.controls.masterslave.ConventionalReactiveVisualFilterFactoryMixin;
import mongoose.backend.controls.masterslave.ConventionalUiBuilder;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Person;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;

import static webfx.framework.client.orm.dql.DqlStatement.where;

final class UsersActivity extends EventDependentViewDomainActivity implements
        ConventionalUiBuilderMixin,
        ConventionalReactiveVisualFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final UsersPresentationModel pm = new UsersPresentationModel();

    @Override
    public UsersPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private ConventionalUiBuilder ui; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "users", "Person");
        return ui.buildUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.onResume();
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualFilter<Person> groupFilter, masterFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<Person>createGroupReactiveVisualFilter(pm, "{class: 'Person', alias: 'p', orderBy: 'id'}")
                // Everything set up, let's start now!
                .start();

        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<Person>createMasterReactiveVisualFilter(pm, "{class: 'Person', alias: 'p', orderBy: 'lastName,firstName,id'}")
                // Applying the user search
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        s.contains("@") ? where("lower(email) like ?", "%" + s.toLowerCase() + "%")
                                : where("abcNames(firstName + ' ' + lastName) like ?", AbcNames.evaluate(s, true) ))
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter .refreshWhenActive();
        masterFilter.refreshWhenActive();
    }
}
