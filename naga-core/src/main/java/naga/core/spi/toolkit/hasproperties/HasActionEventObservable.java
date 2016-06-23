package naga.core.spi.toolkit.hasproperties;

import naga.core.spi.toolkit.events.ActionEvent;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public interface HasActionEventObservable {

    Observable<ActionEvent> actionEventObservable();

}
