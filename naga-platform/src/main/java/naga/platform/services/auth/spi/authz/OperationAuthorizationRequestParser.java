package naga.platform.services.auth.spi.authz;

/**
 * @author Bruno Salmon
 */
public interface OperationAuthorizationRequestParser {

    Object parseOperationAuthorizationRequest(String operationAuthorizationRequest);

}
