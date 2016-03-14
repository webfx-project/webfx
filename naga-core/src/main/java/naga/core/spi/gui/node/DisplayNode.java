package naga.core.spi.gui.node;

import naga.core.ngui.displayresult.DisplayResult;
import rx.Subscriber;

/**
 * @author Bruno Salmon
 */
public interface DisplayNode<N> extends Node<N> {

    Subscriber<DisplayResult> getDisplayResultSubscriber();

}
