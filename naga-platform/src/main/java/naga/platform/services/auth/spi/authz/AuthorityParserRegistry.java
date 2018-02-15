package naga.platform.services.auth.spi.authz;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class AuthorityParserRegistry implements AuthorityParser {

    private final Collection<AuthorityParser> parsers = new ArrayList<>();

    public void registerParser(AuthorityParser parser) {
        parsers.add(parser);
    }

    @Override
    public Object parseAuthority(String authority) {
        for (AuthorityParser parser : parsers) {
            Object parsedAuthority = parser.parseAuthority(authority);
            if (parsedAuthority != null && parsedAuthority != authority)
                return parsedAuthority;
        }
        return null;
    }

}
