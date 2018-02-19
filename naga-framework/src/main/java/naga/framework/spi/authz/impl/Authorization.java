package naga.framework.spi.authz.impl;

/**
 * @author Bruno Salmon
 */
public interface Authorization<R> {

    boolean authorizes(R operationAuthorizationRequest);

    Class<R> operationAuthorizationRequestClass(); // used for registration when coming from parsing

}
