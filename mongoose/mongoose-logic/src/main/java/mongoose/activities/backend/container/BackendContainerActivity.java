package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerActivity;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class BackendContainerActivity extends ContainerActivity<BackendContainerViewModel, BackendContainerPresentationModel> {

    public BackendContainerActivity() {
        super(BackendContainerPresentationModel::new);
    }

    @Override
    protected BackendContainerViewModel buildView(Toolkit toolkit) {
        Button backButton = toolkit.createButton();
        Button forwardButton = toolkit.createButton();
        Button organizationsButton = toolkit.createButton();
        Button eventsButton = toolkit.createButton();
        Button bookingsButton = toolkit.createButton();
        Button lettersButton = toolkit.createButton();
        Button monitorButton = toolkit.createButton();
        Button testerButton = toolkit.createButton();
        Button englishButton = toolkit.createButton();
        Button frenchButton = toolkit.createButton();
        return new BackendContainerViewModel(toolkit.createVPage().setHeader(toolkit.createHBox(backButton, forwardButton, organizationsButton, eventsButton, bookingsButton, lettersButton, monitorButton, testerButton, englishButton, frenchButton)),
                backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, bookingsButton, lettersButton, monitorButton, testerButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(BackendContainerViewModel vm, BackendContainerPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        I18n i18n = getI18n();
        i18n.translateText(vm.getBookingsButton(), "Bookings").actionEventObservable().subscribe(actionEvent -> pm.bookingsButtonActionEventObservable().onNext(actionEvent));
        i18n.translateText(vm.getLettersButton(),  "Letters") .actionEventObservable().subscribe(actionEvent -> pm.lettersButtonActionEventObservable().onNext(actionEvent));
        i18n.translateText(vm.getMonitorButton(),  "Monitor") .actionEventObservable().subscribe(actionEvent -> getHistory().push("/monitor"));
        i18n.translateText(vm.getTesterButton(),   "Tester")  .actionEventObservable().subscribe(actionEvent -> getHistory().push("/tester"));
    }

    @Override
    protected void bindPresentationModelWithLogic(BackendContainerPresentationModel pm) {
        super.bindPresentationModelWithLogic(pm);
        pm.bookingsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        pm.lettersButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
    }
}
