package naga.platform.services.auth.spi.authz;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationParser {

    Authorization parseAuthorization(String authorization);

}
