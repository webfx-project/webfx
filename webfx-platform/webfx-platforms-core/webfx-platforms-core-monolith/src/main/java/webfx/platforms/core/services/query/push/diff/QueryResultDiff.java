package webfx.platforms.core.services.query.push.diff;

import webfx.platforms.core.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public interface QueryResultDiff {

    int getPreviousQueryResultVersionNumber();

    int getFinalQueryResultVersionNumber();

    QueryResult applyTo(QueryResult queryResult);

}
