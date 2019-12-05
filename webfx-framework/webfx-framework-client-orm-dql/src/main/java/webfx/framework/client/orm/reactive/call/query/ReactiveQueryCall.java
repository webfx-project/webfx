package webfx.framework.client.orm.reactive.call.query;

import webfx.framework.client.orm.reactive.call.ReactiveCall;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryService;
import webfx.platform.shared.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ReactiveQueryCall extends ReactiveCall<QueryArgument, QueryResult> {

    public ReactiveQueryCall() {
        this(QueryService::executeQuery);
    }

    public ReactiveQueryCall(AsyncFunction<QueryArgument, QueryResult> queryFunction) {
        super(queryFunction);
    }

}
