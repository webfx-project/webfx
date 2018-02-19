package naga.platform.services.authz.impl;

/**
 * @author Bruno Salmon
 */
public interface OperationAuthorizationRequestParser {

    Object parseOperationAuthorizationRequest(String operationAuthorizationRequest);

}
