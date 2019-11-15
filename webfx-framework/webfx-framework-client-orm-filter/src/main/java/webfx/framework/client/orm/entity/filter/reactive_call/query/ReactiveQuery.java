package webfx.framework.client.orm.entity.filter.reactive_call.query;

import webfx.framework.client.orm.entity.filter.reactive_call.ReactiveCall;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryService;
import webfx.platform.shared.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class ReactiveQuery extends ReactiveCall<QueryArgument, QueryResult> {

    public ReactiveQuery() {
        this(QueryService::executeQuery);
    }

    public ReactiveQuery(AsyncFunction<QueryArgument, QueryResult> queryFunction) {
        super(queryFunction);
    }

}
