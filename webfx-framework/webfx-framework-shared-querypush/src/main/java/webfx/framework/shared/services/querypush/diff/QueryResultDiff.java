package webfx.framework.shared.services.querypush.diff;

import webfx.platform.shared.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public interface QueryResultDiff {

    int getPreviousQueryResultVersionNumber();

    int getFinalQueryResultVersionNumber();

    QueryResult applyTo(QueryResult queryResult);

}
