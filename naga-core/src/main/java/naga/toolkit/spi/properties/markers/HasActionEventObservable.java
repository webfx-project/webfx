package naga.toolkit.spi.properties.markers;

import naga.toolkit.spi.events.ActionEvent;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public interface HasActionEventObservable {

    Observable<ActionEvent> actionEventObservable();

}
