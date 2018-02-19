package naga.framework.spi.authz.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public interface InMemoryAuthorizationRule<R> {

    boolean authorizes(R operationAuthorizationRequest);

    Class<R> operationAuthorizationRequestClass(); // used for registration when coming from parsing

}
