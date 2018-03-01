package naga.framework.operation.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRule;
import naga.framework.spi.authz.impl.inmemory.parser.SimpleInMemoryAuthorizationRuleParserBase;

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
