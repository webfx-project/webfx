package naga.framework.operation.authz;

import naga.framework.services.authz.spi.impl.inmemory.AuthorizationRuleType;
import naga.framework.services.authz.spi.impl.inmemory.InMemoryAuthorizationRule;
import naga.framework.services.authz.spi.impl.inmemory.parser.SimpleInMemoryAuthorizationRuleParserBase;

/**
 * @author Bruno Salmon
 */
public class OperationAuthorizationRuleParser extends SimpleInMemoryAuthorizationRuleParserBase {

    @Override
    protected InMemoryAuthorizationRule parseAuthorization(AuthorizationRuleType type, String argument) {
        if (argument.startsWith("operation:")) {
            String code = argument.substring(10).trim();
            return new OperationAuthorizationRule(type, code);
        }
        return null;
    }
}
