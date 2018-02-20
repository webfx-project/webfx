package naga.framework.spi.authz.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public abstract class SimpleInMemoryAuthorizationRuleBase<R> implements InMemoryAuthorizationRule<R> {

    private final AuthorizationRuleType type;
    private final Class operationAuthorizationRequestClass;

    public SimpleInMemoryAuthorizationRuleBase(AuthorizationRuleType type, Class<R> operationAuthorizationRequestClass) {
        this.type = type;
        this.operationAuthorizationRequestClass = operationAuthorizationRequestClass;
    }

    @Override
    public AuthorizationRuleResult computeRuleResult(R operationAuthorizationRequest) {
        return !matchRule(operationAuthorizationRequest) ? AuthorizationRuleResult.OUT_OF_RULE_CONTEXT
                : type == AuthorizationRuleType.GRANT    ? AuthorizationRuleResult.GRANTED
                                                         : AuthorizationRuleResult.DENIED;
    }

    protected abstract boolean matchRule(R operationAuthorizationRequest);

    @Override
    public Class operationAuthorizationRequestClass() {
        return operationAuthorizationRequestClass;
    }
}
