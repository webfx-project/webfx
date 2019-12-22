package webfx.framework.shared.operation.authz;

import webfx.framework.shared.services.authz.spi.impl.inmemory.AuthorizationRuleType;
import webfx.framework.shared.services.authz.spi.impl.inmemory.InMemoryAuthorizationRule;
import webfx.framework.shared.services.authz.spi.impl.inmemory.parser.SimpleInMemoryAuthorizationRuleParserBase;

/**
 * @author Bruno Salmon
 */
public final class OperationAuthorizationRuleParser extends SimpleInMemoryAuthorizationRuleParserBase {

    @Override
    protected InMemoryAuthorizationRule parseAuthorization(AuthorizationRuleType type, String argument) {
        if (argument.startsWith("operation:")) {
            String code = argument.substring(10).trim();
            return new OperationAuthorizationRule(type, code);
        }
        return null;
    }
}
