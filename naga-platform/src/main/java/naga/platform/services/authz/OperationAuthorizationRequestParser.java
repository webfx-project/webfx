package naga.platform.services.authz;

/**
 * @author Bruno Salmon
 */
public interface OperationAuthorizationRequestParser {

    Object parseOperationAuthorizationRequest(String operationAuthorizationRequest);

}
