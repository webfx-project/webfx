package webfx.framework.operation.authz;

import webfx.framework.operation.HasOperationCode;
import webfx.framework.services.authz.spi.impl.inmemory.AuthorizationRuleType;
import webfx.framework.services.authz.spi.impl.inmemory.SimpleInMemoryAuthorizationRuleBase;

/**
 * @author Bruno Salmon
 */
public class OperationAuthorizationRule extends SimpleInMemoryAuthorizationRuleBase {

    private final Class operationRequestClass;
    private final Object operationRequestCode;

    public OperationAuthorizationRule(AuthorizationRuleType type, Class operationRequestClass) {
        this(type, operationRequestClass, null);

    }

    public OperationAuthorizationRule(AuthorizationRuleType type, Object operationRequestCode) {
        this(type, null, operationRequestCode);
    }

    public OperationAuthorizationRule(AuthorizationRuleType type, Class operationRequestClass, Object operationRequestCode) {
        super(type, operationRequestClass);
        this.operationRequestClass = operationRequestClass;
        this.operationRequestCode = operationRequestCode;
    }

    @Override
    protected boolean matchRule(Object operationRequest) {
        if (operationRequestCode != null && operationRequest instanceof HasOperationCode && operationRequestCode.equals(((HasOperationCode) operationRequest).getOperationCode()))
            return true;
        if (operationRequestClass != null && operationRequestClass.equals(operationRequest.getClass()))
            return true;
        return false;
    }
}
