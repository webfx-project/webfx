package naga.platform.services.auth.spi.authz;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class AuthorizationParserRegistry implements AuthorizationParser {

    private final Collection<AuthorizationParser> parsers = new ArrayList<>();

    public void registerParser(AuthorizationParser parser) {
        parsers.add(parser);
    }

    public Authorization parseAuthorization(String authorization) {
        for (AuthorizationParser parser : parsers) {
            Authorization parserdAuthorization = parser.parseAuthorization(authorization);
            if (parserdAuthorization != null)
                return parserdAuthorization;
        }
        return null;
    }

}
