package webfx.framework.server.services.querypush;

import webfx.framework.server.services.push.PushServerService;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.*;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.framework.shared.services.querypush.PulseArgument;
import webfx.framework.shared.services.querypush.QueryPushResult;
import webfx.framework.shared.services.querypush.QueryPushService;
import webfx.platform.server.services.submitlistener.SubmitListener;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.List;

import static webfx.framework.shared.services.querypush.QueryPushService.QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public final class QueryPushServerService {

    // Server side push of a query result to a specific client
    public static <T> Future<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, BusService.bus(), pushClientId);
    }

    public static class ProvidedSubmitListener implements SubmitListener {

        @Override
        public void onSuccessfulSubmit(SubmitArgument submitArgument) {
            Object submitScope = getOrBuildSubmitSchemaScope(submitArgument);
            QueryPushService.executePulse(PulseArgument.createToRefreshAllQueriesImpactedBySchemaScope(submitArgument.getDataSourceId(), submitScope));
        }

        private static Object getOrBuildSubmitSchemaScope(SubmitArgument submitArgument) {
            Object submitSchemaScope = submitArgument.getSchemaScope();
            if (submitSchemaScope == null) {
                String dqlSubmit = getDqlSubmitStatement(submitArgument);
                if (dqlSubmit != null) {
                    // TODO Introducing a dependency to webfx-framework-shared-orm-domainmodel => see if we can move this into an interceptor in a new separate module
                    DataSourceModel dataSourceModel = DataSourceModelService.getDataSourceModel(submitArgument.getDataSourceId());
                    if (dataSourceModel != null) {
                        // TODO Should we cache this (dqlSubmit => modified fields)?
                        DqlStatement<Object> dqlStatement = dataSourceModel.parseStatement(dqlSubmit);
                        ExpressionArray<?> modifyingExpressions =
                                dqlStatement instanceof Update ? ((Update<Object>) dqlStatement).getSetClause()
                                        : dqlStatement instanceof Insert ? ((Insert<Object>) dqlStatement).getSetClause()
                                        : null;
                        if (modifyingExpressions != null)
                            submitSchemaScope = collectModifiedFields(modifyingExpressions);
                    }
                }
            }
            return submitSchemaScope;
        }

        private static String getDqlSubmitStatement(SubmitArgument submitArgument) {
            SubmitArgument originalArgument = submitArgument.getOriginalArgument();
            return "DQL".equalsIgnoreCase(submitArgument.getLanguage()) ? submitArgument.getStatement()
                    : originalArgument != null && originalArgument != submitArgument ? getDqlSubmitStatement(originalArgument)
                    : null;
        }

        private static List<DomainField> collectModifiedFields(ExpressionArray<?> modifyingExpressions) {
            List<DomainField> modifiedFields = new ArrayList<>();
            for (Expression<?> expression : modifyingExpressions.getExpressions()) {
                if (expression instanceof Equals) {
                    Expression<?> left = ((Equals<?>) expression).getLeft();
                    if (left instanceof DomainField)
                        modifiedFields.add((DomainField) left);
                }
            }
            return modifiedFields;
        }
    }
}
