package naga.platform.services.authz.impl;

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
            Object requiredAccessObject = parser.parseOperationAuthorizationRequest(operationAuthorizationRequest);
            if (requiredAccessObject != null && requiredAccessObject != operationAuthorizationRequest)
                return requiredAccessObject;
        }
        return null;
    }

}
