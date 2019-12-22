package webfx.framework.shared.services.authz.spi.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public abstract class SimpleInMemoryAuthorizationRuleBase<R> implements InMemoryAuthorizationRule<R> {

    private final AuthorizationRuleType type;
    private final Class operationRequestClass;

    public SimpleInMemoryAuthorizationRuleBase(AuthorizationRuleType type, Class<R> operationRequestClass) {
        this.type = type;
        this.operationRequestClass = operationRequestClass;
    }

    @Override
    public AuthorizationRuleResult computeRuleResult(R authorizationRequest) {
        return !matchRule(authorizationRequest) ? AuthorizationRuleResult.OUT_OF_RULE_CONTEXT
                : type == AuthorizationRuleType.GRANT    ? AuthorizationRuleResult.GRANTED
                                                         : AuthorizationRuleResult.DENIED;
    }

    protected abstract boolean matchRule(R operationRequest);

    @Override
    public Class operationRequestClass() {
        return operationRequestClass;
    }
}
