package mongoose.authz;

import mongoose.authn.MongooseUserPrincipal;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.router.auth.authz.RouteAuthorizationRule;
import naga.framework.router.auth.authz.RouteAuthorizationRuleParser;
import naga.framework.router.auth.authz.RouteOperationAuthorizationRequestParser;
import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRuleRegistry;
import naga.framework.spi.authz.impl.inmemory.InMemoryUserPrincipalAuthorizationChecker;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.spi.QueryService;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
class MongooseInMemoryUserPrincipalAuthorizationChecker extends InMemoryUserPrincipalAuthorizationChecker {

    MongooseInMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, DataSourceModel dataSourceModel) {
        super(userPrincipal, new InMemoryAuthorizationRuleRegistry());
        // Registering the authorization (requests and rules) parsers
        ruleRegistry.addOperationAuthorizationRequestParser(new RouteOperationAuthorizationRequestParser());
        ruleRegistry.addInMemoryAuthorizationRuleParser(new RouteAuthorizationRuleParser());
        // Loading the authorizations assigned to the user
        MongooseUserPrincipal principal = (MongooseUserPrincipal) userPrincipal;
        Object[] parameters = {principal.getUserPersonId()};
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect("select rule.rule,activityState.route from AuthorizationAssignment where active and management.user=?", parameters);
        setUpInMemoryAsyncLoading(QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, dataSourceModel.getId())), ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else // When successfully loaded, iterating over the assignments
                for (Entity assignment: QueryResultSetToEntityListGenerator.createEntityList(ar.result(), sqlCompiled.getQueryMapping(), EntityStore.create(dataSourceModel), "assignments")) {
                    // If it is an authorization rule assignment, registering it
                    Entity authorizationRule = assignment.getForeignEntity("rule");
                    if (authorizationRule != null) // if yes, passing the rule as a string (will be parsed)
                        ruleRegistry.registerInMemoryAuthorizationRule(authorizationRule.getStringFieldValue("rule"));
                    // If it is a shared activity state, automatically granting the route to it (when provided)
                    Entity activityState = assignment.getForeignEntity("activityState");
                    if (activityState != null) {
                        String route = activityState.getStringFieldValue("route");
                        if (route != null) {
                            route = Strings.replaceAll(route, "[id]", activityState.getPrimaryKey());
                            ruleRegistry.registerInMemoryAuthorizationRule(new RouteAuthorizationRule(AuthorizationRuleType.GRANT, route, false));
                        }
                    }
                }
        });
    }
}
