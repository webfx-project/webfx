package webfx.framework.client.orm.reactive.call;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import webfx.platform.shared.services.log.Logger;

import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public class SwitchableReactiveCall<A,R> extends ReactiveCall<A, R> {

    public SwitchableReactiveCall() {
        super(null);
    }

    private final ObjectProperty<ReactiveCall<A, R>> delegateProperty = new SimpleObjectProperty<ReactiveCall<A, R>/*GWT*/>() {
        private ReactiveCall<A, R> lastDelegate;
        @Override
        protected void invalidated() {
            ReactiveCall<A, R> newDelegate = get();
            onDelegateChanged(lastDelegate, newDelegate);
            lastDelegate = newDelegate;
        }
    };

    public ObjectProperty<ReactiveCall<A, R>> delegateProperty() {
        return delegateProperty;
    }

    public ReactiveCall<A, R> getDelegate() {
        return delegateProperty.get();
    }

    public void setDelegate(ReactiveCall<A, R> delegate) {
        this.delegateProperty.set(delegate);
    }

    private void onDelegateChanged(ReactiveCall<A, R> lastDelegate, ReactiveCall<A, R> newDelegate) {
        unbindDelegate(lastDelegate);
        bindDelegate(newDelegate);
    }

    private void unbindDelegate(ReactiveCall<A, R> delegate) {
        if (delegate != null) {
            delegate.activeProperty().unbind();
            delegate.argumentProperty().unbind();
            delegate.autoRefreshDelayProperty().unbind();
            delegate.startedProperty().unbind();
            delegate.stop();
        }
    }

    private void bindDelegate(ReactiveCall<A, R> delegate) {
        if (delegate != null) {
            delegate.activeProperty().bind(activeProperty());
            delegate.autoRefreshDelayProperty().bind(autoRefreshDelayProperty());
            delegate.startedProperty().bind(startedProperty());
            Supplier<A> argumentFetcher = getArgumentFetcher();
            delegate.setArgumentFetcher(argumentFetcher);
            if (argumentFetcher == null)
                delegate.argumentProperty().bind(argumentProperty());
            else
                delegate.argumentProperty().unbind();
            callingProperty.bind(delegate.callingProperty());
            resultProperty.bind(delegate.resultProperty());
            callExceptionProperty.bind(delegate.callExceptionProperty());
        } else {
            callingProperty.unbind();
            resultProperty.unbind();
            callExceptionProperty.unbind();
        }
    }

    @Override
    public void setArgumentFetcher(Supplier<A> argumentFetcher) {
        super.setArgumentFetcher(argumentFetcher);
        ReactiveCall<A, R> delegate = getDelegate();
        if (delegate != null) {
            delegate.setArgumentFetcher(argumentFetcher);
            if (argumentFetcher == null)
                delegate.argumentProperty().bind(argumentProperty());
            else
                delegate.argumentProperty().unbind();
        }
    }

    @Override
    public void onArgumentChanged() {
        ReactiveCall<A, R> delegate = getDelegate();
        if (delegate != null)
            delegate.onArgumentChanged();
    }

    @Override
    protected A getLastCallArgument() {
        return getDelegate().getLastCallArgument();
    }

    @Override
    public void refreshWhenReady(boolean force) {
        ReactiveCall<A, R> delegate = getDelegate();
        if (delegate != null)
            delegate.refreshWhenReady(force);
    }

    @Override
    protected void onStarted() {
        bindDelegate(getDelegate());
    }

    @Override
    protected void onStopped() {
        unbindDelegate(getDelegate());
    }

    @Override
    protected void scheduleFireCallNowIfRequired() {
        ReactiveCall<A, R> delegate = getDelegate();
        if (delegate != null)
            delegate.scheduleFireCallNowIfRequired();
    }

    @Override
    protected void fireCall() {
        // Ignoring all calls at that level as they are all delegated through binding
        Logger.log("fireCall() called in SwitchableReactiveCall!!!");
    }
}
