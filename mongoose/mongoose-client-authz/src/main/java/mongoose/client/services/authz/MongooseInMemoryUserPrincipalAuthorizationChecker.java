package mongoose.client.services.authz;

import mongoose.client.services.authn.MongooseUserPrincipal;
import webfx.framework.shared.operation.authz.OperationAuthorizationRuleParser;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.router.auth.authz.RoutingAuthorizationRule;
import webfx.framework.shared.router.auth.authz.RoutingAuthorizationRuleParser;
import webfx.framework.shared.services.authz.spi.impl.inmemory.AuthorizationRuleType;
import webfx.framework.shared.services.authz.spi.impl.inmemory.InMemoryUserPrincipalAuthorizationChecker;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
final class MongooseInMemoryUserPrincipalAuthorizationChecker extends InMemoryUserPrincipalAuthorizationChecker {

    MongooseInMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, DataSourceModel dataSourceModel) {
        super(userPrincipal);
        // userPrincipal must be a MongooseUserPrincipal
        MongooseUserPrincipal principal = (MongooseUserPrincipal) userPrincipal;
        // Registering the authorization (requests and rules) parsers
        ruleRegistry.addAuthorizationRuleParser(new RoutingAuthorizationRuleParser());
        ruleRegistry.addAuthorizationRuleParser(new OperationAuthorizationRuleParser());
        if (userPrincipal != null)
            setUpInMemoryAsyncRulesLoading(EntityStore.create(dataSourceModel).executeQuery("select rule.rule,activityState.route from AuthorizationAssignment where active and management.user=?", principal.getUserPersonId()), ar -> {
                if (ar.failed())
                    Logger.log(ar.cause());
                else // When successfully loaded, iterating over the assignments
                    for (Entity assignment: ar.result()) {
                        // If it is an authorization rule assignment, registering it
                        Entity authorizationRule = assignment.getForeignEntity("rule");
                        if (authorizationRule != null) // if yes, passing the rule as a string (will be parsed)
                            ruleRegistry.registerAuthorizationRule(authorizationRule.getStringFieldValue("rule"));
                        // If it is a shared activity state, automatically granting the route to it (when provided)
                        Entity activityState = assignment.getForeignEntity("activityState");
                        if (activityState != null) {
                            String route = activityState.getStringFieldValue("route");
                            if (route != null) {
                                route = Strings.replaceAll(route, "[id]", activityState.getPrimaryKey());
                                ruleRegistry.registerAuthorizationRule(new RoutingAuthorizationRule(AuthorizationRuleType.GRANT, route, false));
                            }
                        }
                    }
            });
    }
}
