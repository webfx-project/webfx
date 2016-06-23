package mongoose.activities.container;

import naga.core.ui.presentation.PresentationModel;
import naga.core.spi.toolkit.events.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
class ContainerPresentationModel implements PresentationModel {

    // Display input

    private final BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable() { return bookingsButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> organizationButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> organizationsButtonActionEventObservable() { return organizationButtonActionEventObservable; }

}
