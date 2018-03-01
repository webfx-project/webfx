package naga.framework.spi.authz.impl.inmemory.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class AuthorizationRequestParserRegistry implements AuthorizationRequestParser {

    private final Collection<AuthorizationRequestParser> parsers = new ArrayList<>();

    public void registerParser(AuthorizationRequestParser parser) {
        parsers.add(parser);
    }

    @Override
    public Object parseAuthorizationRequest(String authorizationRequest) {
        for (AuthorizationRequestParser parser : parsers) {
            Object parsedOperationAuthorizationRequest = parser.parseAuthorizationRequest(authorizationRequest);
            if (parsedOperationAuthorizationRequest != null && parsedOperationAuthorizationRequest != authorizationRequest)
                return parsedOperationAuthorizationRequest;
        }
        return null;
    }

}
