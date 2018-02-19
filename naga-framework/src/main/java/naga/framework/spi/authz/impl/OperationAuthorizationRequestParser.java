package naga.framework.spi.authz.impl;

/**
 * @author Bruno Salmon
 */
public interface OperationAuthorizationRequestParser {

    Object parseOperationAuthorizationRequest(String operationAuthorizationRequest);

}
