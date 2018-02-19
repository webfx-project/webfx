package naga.framework.spi.authz.impl.inmemory.parser;

/**
 * @author Bruno Salmon
 */
public interface OperationAuthorizationRequestParser {

    Object parseOperationAuthorizationRequest(String operationAuthorizationRequest);

}
