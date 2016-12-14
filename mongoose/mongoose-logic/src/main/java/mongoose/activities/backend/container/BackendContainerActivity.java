package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerActivity;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.FlowPane;
import naga.toolkit.spi.Toolkit;
/**
 * @author Bruno Salmon
 */
public class BackendContainerActivity extends ContainerActivity<BackendContainerViewModel, BackendContainerPresentationModel> {

    public BackendContainerActivity() {
        super(BackendContainerPresentationModel::new);
    }

    @Override
    protected BackendContainerViewModel buildView(Toolkit toolkit) {
        Button backButton = Button.create();
        Button forwardButton = Button.create();
        Button organizationsButton = Button.create();
        Button eventsButton = Button.create();
        Button bookingsButton = Button.create();
        Button lettersButton = Button.create();
        Button monitorButton = Button.create();
        Button testerButton = Button.create();
        Button englishButton = Button.create();
        Button frenchButton = Button.create();
        return new BackendContainerViewModel(BorderPane.create(null, FlowPane.create(backButton, forwardButton, organizationsButton, eventsButton, bookingsButton, lettersButton, monitorButton, testerButton, englishButton, frenchButton), null, null, null),
                backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, bookingsButton, lettersButton, monitorButton, testerButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(BackendContainerViewModel vm, BackendContainerPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        I18n i18n = getI18n();
        i18n.translateText(vm.getBookingsButton(), "Bookings").setOnMouseClicked(event -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        i18n.translateText(vm.getLettersButton(),  "Letters") .setOnMouseClicked(event -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
        i18n.translateText(vm.getMonitorButton(),  "Monitor") .setOnMouseClicked(event -> getHistory().push("/monitor"));
        i18n.translateText(vm.getTesterButton(),   "Tester")  .setOnMouseClicked(event -> getHistory().push("/tester"));
    }

    @Override
    protected void bindPresentationModelWithLogic(BackendContainerPresentationModel pm) {
        super.bindPresentationModelWithLogic(pm);
/*
        pm.bookingsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        pm.lettersButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
*/
    }
}
