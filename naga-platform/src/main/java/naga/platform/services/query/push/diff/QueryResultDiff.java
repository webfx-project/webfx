package naga.platform.services.query.push.diff;

import naga.platform.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public interface QueryResultDiff {

    QueryResult applyTo(QueryResult queryResult);

}
