package naga.platform.services.authz;

/**
 * @author Bruno Salmon
 */
public interface Authorization<R> {

    boolean authorizes(R operationAuthorizationRequest);

    Class<R> operationAuthorizationRequestClass(); // used for registration when coming from parsing

}
