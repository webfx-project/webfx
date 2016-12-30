package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerActivity;
import mongoose.activities.shared.theme.DarkTheme;
import mongoose.activities.shared.theme.LightTheme;
import mongoose.activities.shared.theme.Theme;
import naga.framework.ui.i18n.I18n;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.layout.FlowPane;

/**
 * @author Bruno Salmon
 */
public class BackendContainerActivity extends ContainerActivity<BackendContainerViewModel, BackendContainerPresentationModel> {

    public BackendContainerActivity() {
        super(BackendContainerPresentationModel::new);
    }

    @Override
    protected BackendContainerViewModel buildView() {
        BackendContainerViewModel vm = new BackendContainerViewModel();
        BorderPane borderPane = vm.getContentNode();
        borderPane.setTop(new FlowPane(vm.getBackButton(), vm.getForwardButton(), vm.getOrganizationsButton(), vm.getEventsButton(), vm.getBookingsButton(), vm.getLettersButton(), vm.getMonitorButton(), vm.getTesterButton(), vm.getEnglishButton(), vm.getFrenchButton(), vm.getLightTheme(), vm.getDarkTheme()));
        borderPane.backgroundProperty().bind(Theme.mainBackgroundProperty());
        vm.getBackButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getForwardButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getOrganizationsButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getEventsButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getBookingsButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getLettersButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getMonitorButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getEnglishButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getFrenchButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getTesterButton().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getLightTheme().textFillProperty().bind(Theme.mainTextFillProperty());
        vm.getDarkTheme().textFillProperty().bind(Theme.mainTextFillProperty());
        return vm;
    }

    @Override
    protected void bindViewModelWithPresentationModel(BackendContainerViewModel vm, BackendContainerPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        I18n i18n = getI18n();
        i18n.translateText(vm.getBookingsButton(), "Bookings").setOnMouseClicked(event -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        i18n.translateText(vm.getLettersButton(),  "Letters") .setOnMouseClicked(event -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
        i18n.translateText(vm.getMonitorButton(),  "Monitor") .setOnMouseClicked(event -> getHistory().push("/monitor"));
        i18n.translateText(vm.getTesterButton(),   "Tester")  .setOnMouseClicked(event -> getHistory().push("/tester"));
        i18n.translateText(vm.getLightTheme(),  "Light") .setOnMouseClicked(e -> new LightTheme().apply());
        i18n.translateText(vm.getDarkTheme(),   "Dark")  .setOnMouseClicked(e -> new DarkTheme().apply());
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
