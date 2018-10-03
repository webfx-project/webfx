package webfx.framework.shared.services.authz.spi.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public interface InMemoryAuthorizationRule<R> {

    AuthorizationRuleResult computeRuleResult(R authorizationRequest);

    Class<R> operationRequestClass(); // used for registration when coming from parsing

}
