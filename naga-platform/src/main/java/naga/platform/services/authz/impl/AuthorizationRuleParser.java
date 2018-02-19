package naga.platform.services.authz.impl;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationRuleParser {

    Authorization parseAuthorization(String authorizationRule);

}
