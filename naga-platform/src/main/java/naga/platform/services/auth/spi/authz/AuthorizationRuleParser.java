package naga.platform.services.auth.spi.authz;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationRuleParser {

    Authorization parseAuthorization(String authorizationRule);

}
