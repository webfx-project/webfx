package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerPresentationModel;
import javafx.event.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
class BackendContainerPresentationModel extends ContainerPresentationModel {

    private final BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> bookingsButtonActionEventObservable() { return bookingsButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> lettersButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> lettersButtonActionEventObservable() { return lettersButtonActionEventObservable; }

}
