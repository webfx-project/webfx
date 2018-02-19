package naga.framework.spi.authz.impl;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationRuleParser {

    Authorization parseAuthorization(String authorizationRule);

}
