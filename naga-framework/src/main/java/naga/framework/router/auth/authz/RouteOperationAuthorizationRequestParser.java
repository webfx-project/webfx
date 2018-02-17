package naga.framework.router.auth.authz;

import naga.platform.services.authz.OperationAuthorizationRequestParser;

/**
 * @author Bruno Salmon
 */
public class RouteOperationAuthorizationRequestParser implements OperationAuthorizationRequestParser {

    @Override
    public Object parseOperationAuthorizationRequest(String operationAuthorizationRequest) {
        if (operationAuthorizationRequest.startsWith("route:"))
            return new RouteAuthorizationRequest(operationAuthorizationRequest.substring(6).trim());
        return operationAuthorizationRequest;
    }

}
