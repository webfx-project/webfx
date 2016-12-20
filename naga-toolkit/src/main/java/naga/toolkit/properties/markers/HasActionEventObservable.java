package naga.toolkit.properties.markers;

import naga.toolkit.fx.event.ActionEvent;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public interface HasActionEventObservable {

    Observable<ActionEvent> actionEventObservable();

}
