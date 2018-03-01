package naga.framework.spi.authz.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public interface InMemoryAuthorizationRule<R> {

    AuthorizationRuleResult computeRuleResult(R authorizationRequest);

    Class<R> operationRequestClass(); // used for registration when coming from parsing

}
