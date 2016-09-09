package mongoose.activities.container;

import naga.toolkit.spi.events.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
class BackendContainerPresentationModel extends FrontendContainerPresentationModel {

    private final BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable() { return bookingsButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> lettersButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> lettersButtonActionEventObservable() { return lettersButtonActionEventObservable; }

}
