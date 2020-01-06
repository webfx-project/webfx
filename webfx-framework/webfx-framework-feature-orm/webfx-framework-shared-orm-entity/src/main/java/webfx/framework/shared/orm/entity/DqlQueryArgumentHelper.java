package webfx.framework.shared.orm.entity;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.platform.shared.datascope.aggregate.AggregateScope;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class DqlQueryArgumentHelper {

    private final static String DQL_LANGUAGE = "DQL";
    private final static String QUERY_LANGUAGE = DQL_LANGUAGE; // can be set to "DQL", "SQL" or null (null = default = SQL)

    public static QueryArgument createQueryArgument(String dqlQuery, Object[] parameters, DataSourceModel dataSourceModel, AggregateScope aggregateScope) {
        return QueryArgument.builder()
                .setLanguage(DQL_LANGUAGE)
                .setStatement(DQL_LANGUAGE.equals(QUERY_LANGUAGE) ? dqlQuery : dataSourceModel.translateQuery(DQL_LANGUAGE, dqlQuery))
                .setParameters(resolveParameters(parameters))
                .setDataSourceId(dataSourceModel.getDataSourceId())
                .addDataScope(aggregateScope)
                .build();
    }

    private static Object[] resolveParameters(Object[] parameters) {
        if (parameters != null) {
            boolean hasResolved = false;
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                Object primaryKey = Entities.getPrimaryKey(parameter);
                if (primaryKey != parameter) {
                    if (!hasResolved) {
                        parameters = Arrays.clone(parameters, Object[]::new);
                        hasResolved = true;
                    }
                    parameters[i] = primaryKey;
                }
            }
        }
        return parameters;
    }
}
