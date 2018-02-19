package naga.framework.spi.authz.impl.inmemory.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class OperationAuthorizationRequestParserRegistry implements OperationAuthorizationRequestParser {

    private final Collection<OperationAuthorizationRequestParser> parsers = new ArrayList<>();

    public void registerParser(OperationAuthorizationRequestParser parser) {
        parsers.add(parser);
    }

    @Override
    public Object parseOperationAuthorizationRequest(String operationAuthorizationRequest) {
        for (OperationAuthorizationRequestParser parser : parsers) {
            Object parsedOperationAuthorizationRequest = parser.parseOperationAuthorizationRequest(operationAuthorizationRequest);
            if (parsedOperationAuthorizationRequest != null && parsedOperationAuthorizationRequest != operationAuthorizationRequest)
                return parsedOperationAuthorizationRequest;
        }
        return null;
    }

}
