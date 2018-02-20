package naga.framework.spi.authz.impl.inmemory.parser;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRule;

/**
 * @author Bruno Salmon
 */
public abstract class SimpleInMemoryAuthorizationRuleParserBase implements InMemoryAuthorizationRuleParser {

    @Override
    public InMemoryAuthorizationRule parseAuthorization(String authorizationRule) {
        AuthorizationRuleType type = authorizationRule.startsWith("grant ") || authorizationRule.startsWith("GRANT ") ? AuthorizationRuleType.GRANT
                : authorizationRule.startsWith("revoke ") || authorizationRule.startsWith("REVOKE ") ? AuthorizationRuleType.REVOKE
                : null;
        return type == null ? null : parseAuthorization(type, authorizationRule.substring(6).trim());
    }

    protected abstract InMemoryAuthorizationRule parseAuthorization(AuthorizationRuleType type, String argument);
}
