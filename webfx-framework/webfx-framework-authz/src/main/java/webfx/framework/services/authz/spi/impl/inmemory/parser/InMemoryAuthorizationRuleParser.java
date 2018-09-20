package webfx.framework.services.authz.spi.impl.inmemory.parser;

import webfx.framework.services.authz.spi.impl.inmemory.InMemoryAuthorizationRule;

/**
 * @author Bruno Salmon
 */
public interface InMemoryAuthorizationRuleParser {

    InMemoryAuthorizationRule parseAuthorization(String authorizationRule);

}
