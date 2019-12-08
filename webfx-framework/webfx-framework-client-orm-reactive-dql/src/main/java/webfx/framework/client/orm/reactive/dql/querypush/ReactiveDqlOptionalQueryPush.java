package webfx.framework.client.orm.reactive.dql.querypush;

import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.client.orm.reactive.call.query.ReactiveQueryCall;
import webfx.framework.client.orm.reactive.call.query.push.ReactiveQueryOptionalPush;
import webfx.framework.client.orm.reactive.call.query.push.ReactiveQueryPushCall;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;

/**
 * @author Bruno Salmon
 */
public final class ReactiveDqlOptionalQueryPush<E> extends ReactiveDqlQuery<E> {

    public ReactiveDqlOptionalQueryPush(ReactiveDqlStatement<E> reactiveDqlStatement) {
        super(reactiveDqlStatement, new ReactiveQueryOptionalPush(new ReactiveQueryCall(), new ReactiveQueryPushCall()));
    }
}
