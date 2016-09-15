package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FeesActivity extends BookingProcessActivity<FeesViewModel, FeesPresentationModel> {

    public FeesActivity() {
        super(FeesPresentationModel::new, "options");
    }

    protected FeesViewModel buildView(Toolkit toolkit) {
        Button previousButton = toolkit.createButton();
        Button termsButton = toolkit.createButton();
        Button programButton = toolkit.createButton();
        Button nextButton = toolkit.createButton();
        return new FeesViewModel(toolkit.createVPage().setFooter(toolkit.createHBox(previousButton, termsButton, programButton, nextButton)), previousButton, nextButton, termsButton, programButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(FeesViewModel vm, FeesPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        Button programButton = vm.getProgramButton();
        programButton.setText("Program »");
        programButton.actionEventObservable().subscribe(this::onProgramButtonPressed);
        Button termsButton = vm.getTermsButton();
        termsButton.setText("Terms & conditions »");
        termsButton.actionEventObservable().subscribe(this::onTermsButtonPressed);
    }

    private void onProgramButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage("program");
    }

    private void onTermsButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage("terms");
    }
}
