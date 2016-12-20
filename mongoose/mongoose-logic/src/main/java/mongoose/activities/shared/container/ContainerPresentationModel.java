package mongoose.activities.shared.container;

import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.fx.event.ActionEvent;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class ContainerPresentationModel implements PresentationModel {

    // Display input

    private final BehaviorSubject<ActionEvent> organizationButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> organizationsButtonActionEventObservable() { return organizationButtonActionEventObservable; }

    private final BehaviorSubject<ActionEvent> eventsButtonActionEventObservable = BehaviorSubject.create();
    BehaviorSubject<ActionEvent> eventsButtonActionEventObservable() { return eventsButtonActionEventObservable; }

}
