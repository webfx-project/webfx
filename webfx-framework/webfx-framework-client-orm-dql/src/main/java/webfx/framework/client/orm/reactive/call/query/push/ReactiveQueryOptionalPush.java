package webfx.framework.client.orm.reactive.call.query.push;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import webfx.framework.client.orm.reactive.call.query.ReactiveQueryCall;
import webfx.framework.client.orm.reactive.call.SwitchableReactiveCall;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public final class ReactiveQueryOptionalPush extends SwitchableReactiveCall<QueryArgument, QueryResult> {

    private final ReactiveQueryCall reactiveQueryCall;
    private final ReactiveQueryPushCall reactiveQueryPush;

    public ReactiveQueryOptionalPush(ReactiveQueryCall reactiveQueryCall, ReactiveQueryPushCall reactiveQueryPush) {
        this.reactiveQueryCall = reactiveQueryCall;
        this.reactiveQueryPush = reactiveQueryPush;
    }

    public ReactiveQueryCall getReactiveQueryCall() {
        return reactiveQueryCall;
    }

    public ReactiveQueryPushCall getReactiveQueryPush() {
        return reactiveQueryPush;
    }

    private final BooleanProperty pushProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            updateDelegate();
        }
    };

    public BooleanProperty pushProperty() {
        return pushProperty;
    }

    public boolean isPush() {
        return pushProperty.get();
    }

    public void setPush(boolean push) {
        pushProperty.set(push);
    }

    private void updateDelegate() {
        setDelegate(isPush() ? reactiveQueryPush : reactiveQueryCall);
    }

    @Override
    protected void onStarted() {
        updateDelegate();
        super.onStarted();
    }
}
