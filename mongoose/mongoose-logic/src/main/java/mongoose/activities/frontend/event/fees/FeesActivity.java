package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.DateInfo;
import mongoose.entities.Option;
import naga.commons.type.PrimType;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.rx.RxScheduler;
import naga.framework.ui.rx.RxUi;
import naga.platform.spi.Platform;
import naga.toolkit.display.*;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Table;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FeesActivity extends BookingProcessActivity<FeesViewModel, FeesPresentationModel> {

    public FeesActivity() {
        super(FeesPresentationModel::new, "options");
        registerViewBuilder(getClass(), new FeesViewModelBuilder());
    }

    @Override
    protected void bindViewModelWithPresentationModel(FeesViewModel vm, FeesPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        I18n i18n = getI18n();
        i18n.translateText(setImage(vm.getProgramButton(), "{url: 'images/calendar.svg', width: 16, height: 16}"), "Program")
                .actionEventObservable().subscribe(this::onProgramButtonPressed);
        i18n.translateText(setImage(vm.getTermsButton(), "{url: 'images/certificate.svg', width: 16, height: 16}"), "TermsAndConditions")
                .actionEventObservable().subscribe(this::onTermsButtonPressed);
        vm.getDateInfoCollator().displayResultSetProperty().bind(pm.dateInfoDisplayResultSetProperty());
    }

    private void onProgramButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage("program");
    }

    private void onTermsButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage("terms");
    }

    protected void bindPresentationModelWithLogic(FeesPresentationModel pm) {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                FeesGroup[] feesGroups = async.result();
                I18n i18n = getI18n();
                Observable.combineLatest(
                        RxUi.observe(i18n.dictionaryProperty()),
                        RxUi.observe(activeProperty()),
                        (dictionary, active) -> active)
                        .filter(active -> active)
                        .observeOn(RxScheduler.UI_SCHEDULER)
                        .subscribe(active -> displayFeesGroups(feesGroups, pm));
                onEventAvailabilities().setHandler(ar -> {
                    if (ar.succeeded())
                        displayFeesGroups(feesGroups, pm);
                });
            }
        });
    }

    private Future<FeesGroup[]> onFeesGroup() {
        return onEventOptions().map(this::createFeesGroups);
    }

    private void displayFeesGroups(FeesGroup[] feesGroups, FeesPresentationModel pm) {
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(feesGroups.length, new DisplayColumn[]{
                DisplayColumn.create("FeesGroup", PrimType.STRING),
                DisplayColumn.create(value -> {
                    Table table = Toolkit.get().createTable();
                    table.setDisplayResultSet((DisplayResultSet) value);
                    return table;
                })});
        int inlineIndex = 0;
        I18n i18n = getI18n();
        for (FeesGroup feesGroup : feesGroups) {
            rsb.setInlineValue(inlineIndex++, feesGroup.getDisplayName(i18n));
            rsb.setInlineValue(inlineIndex++, feesGroup.generateDisplayResultSet(i18n, this, this::onBookButtonPressed));
        }
        DisplayResultSet rs = rsb.build();
        pm.dateInfoDisplayResultSetProperty().setValue(rs);
    }

    private FeesGroup[] createFeesGroups() {
        List<FeesGroup> feesGroups = new ArrayList<>();
        EntityList<DateInfo> dateInfos = getEventDateInfos();
        List<Option> defaultOptions = selectDefaultOptions();
        List<Option> accommodationOptions = selectOptions(o -> o.isConcrete() && o.isAccommodation());
        if (dateInfos.isEmpty())
            populateFeesGroups(null, defaultOptions, accommodationOptions, feesGroups);
        else
            for (DateInfo dateInfo : dateInfos)
                populateFeesGroups(dateInfo, defaultOptions, accommodationOptions, feesGroups);
        return Collections.toArray(feesGroups, FeesGroup[]::new);
    }

    private void populateFeesGroups(DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions, List<FeesGroup> feesGroups) {
        feesGroups.add(createFeesGroup(dateInfo, defaultOptions, accommodationOptions));
    }

    private FeesGroup createFeesGroup(DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions) {
        return new FeesGroupBuilder()
                .setDateInfo(dateInfo)
                .setEvent(getEvent())
                .setDefaultOptions(defaultOptions)
                .setAccommodationOptions(accommodationOptions)
                .build();
    }

    private void onBookButtonPressed(OptionsPreselection optionsPreselection) {
        Platform.log("Booking " + optionsPreselection);
        onNextButtonPressed(null);
    }
}
