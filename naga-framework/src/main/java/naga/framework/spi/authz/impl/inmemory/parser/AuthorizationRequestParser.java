package naga.framework.spi.authz.impl.inmemory.parser;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationRequestParser {

    Object parseAuthorizationRequest(String authorizationRequest);

}
