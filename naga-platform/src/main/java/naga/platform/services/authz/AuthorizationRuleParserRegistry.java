package naga.platform.services.authz;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class AuthorizationRuleParserRegistry implements AuthorizationRuleParser {

    private final Collection<AuthorizationRuleParser> parsers = new ArrayList<>();

    public void registerParser(AuthorizationRuleParser parser) {
        parsers.add(parser);
    }

    public Authorization parseAuthorization(String authorizationRule) {
        for (AuthorizationRuleParser parser : parsers) {
            Authorization parserdAuthorization = parser.parseAuthorization(authorizationRule);
            if (parserdAuthorization != null)
                return parserdAuthorization;
        }
        return null;
    }

}
