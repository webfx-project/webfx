package naga.platform.services.authz;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationRuleParser {

    Authorization parseAuthorization(String authorizationRule);

}
