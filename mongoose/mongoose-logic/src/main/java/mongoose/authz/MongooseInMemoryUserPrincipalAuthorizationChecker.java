package mongoose.authz;

import mongoose.authn.MongooseUserPrincipal;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.router.auth.authz.RouteAuthorizationRuleParser;
import naga.framework.router.auth.authz.RouteOperationAuthorizationRequestParser;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRuleRegistry;
import naga.framework.spi.authz.impl.inmemory.InMemoryUserPrincipalAuthorizationChecker;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.spi.QueryService;

/**
 * @author Bruno Salmon
 */
class MongooseInMemoryUserPrincipalAuthorizationChecker extends InMemoryUserPrincipalAuthorizationChecker {

    MongooseInMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, DataSourceModel dataSourceModel) {
        super(userPrincipal, new InMemoryAuthorizationRuleRegistry());
        ruleRegistry.addOperationAuthorizationRequestParser(new RouteOperationAuthorizationRequestParser());
        ruleRegistry.addInMemoryAuthorizationRuleParser(new RouteAuthorizationRuleParser());
        MongooseUserPrincipal principal = (MongooseUserPrincipal) userPrincipal;
        Object[] parameters = {principal.getUserPersonId()};
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect("select rule.rule from AuthorizationAssignment where active and management.user=?", parameters);
        setUpInMemoryAsyncLoading(QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, dataSourceModel.getId())), ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else {
                EntityStore store = EntityStore.create(dataSourceModel);
                EntityList<Entity> assignments = QueryResultSetToEntityListGenerator.createEntityList(ar.result(), sqlCompiled.getQueryMapping(), store, "assignments");
                for (Entity assignment: assignments)
                    ruleRegistry.registerInMemoryAuthorizationRule(assignment.getForeignEntity("rule").getStringFieldValue("rule"));
            }
        });
    }
}
