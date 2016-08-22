package mongoose.activities.container;

import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.spi.events.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
class ContainerPresentationModel implements PresentationModel {

    // Display input

    private final BehaviorSubject<ActionEvent> organizationButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> organizationsButtonActionEventObservable() { return organizationButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> eventsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> eventsButtonActionEventObservable() { return eventsButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable() { return bookingsButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> lettersButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> lettersButtonActionEventObservable() { return lettersButtonActionEventObservable; }

}
