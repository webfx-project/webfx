package naga.platform.services.authz;

import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class AuthorizationRegistry implements Authorization {

    private final Map<Class, Collection<Authorization>> registeredAuthorizations = new HashMap<>();
    private AuthorizationRuleParser authorizationRuleParser;
    private OperationAuthorizationRequestParser operationAuthorizationRequestParser;

    public void setAuthorizationRuleParser(AuthorizationRuleParser authorizationRuleParser) {
        this.authorizationRuleParser = authorizationRuleParser;
    }

    public void addAuthorizationRuleParser(AuthorizationRuleParser authorizationRuleParser) {
        if (this.authorizationRuleParser == null)
            this.authorizationRuleParser = authorizationRuleParser;
        else {
            AuthorizationRuleParserRegistry registry;
            if (this.authorizationRuleParser instanceof AuthorizationRuleParserRegistry)
                registry = (AuthorizationRuleParserRegistry) this.authorizationRuleParser;
            else {
                registry = new AuthorizationRuleParserRegistry();
                registry.registerParser(this.authorizationRuleParser);
            }
            registry.registerParser(authorizationRuleParser);
        }
    }

    public void setOperationAuthorizationRequestParser(OperationAuthorizationRequestParser operationAuthorizationRequestParser) {
        this.operationAuthorizationRequestParser = operationAuthorizationRequestParser;
    }

    public void addOperationAuthorizationRequestParser(OperationAuthorizationRequestParser operationAuthorizationRequestParser) {
        if (this.operationAuthorizationRequestParser == null)
            this.operationAuthorizationRequestParser = operationAuthorizationRequestParser;
        else {
            OperationAuthorizationRequestParserRegistry registry;
            if (this.operationAuthorizationRequestParser instanceof AuthorizationRuleParserRegistry)
                registry = (OperationAuthorizationRequestParserRegistry) this.operationAuthorizationRequestParser;
            else {
                registry = new OperationAuthorizationRequestParserRegistry();
                registry.registerParser(this.operationAuthorizationRequestParser);
            }
            registry.registerParser(operationAuthorizationRequestParser);
        }
    }

    public <A> void registerAuthorization(Class<A> requiredAuthorizationClass, Authorization<A> authorization) {
        Collection<Authorization> authorizations = registeredAuthorizations.get(requiredAuthorizationClass);
        if (authorizations == null)
            registeredAuthorizations.put(requiredAuthorizationClass, authorizations = new ArrayList<>());
        authorizations.add(authorization);
    }

    public <A> void registerAuthorization(Authorization authorization) {
        registerAuthorization(authorization.operationAuthorizationRequestClass(), authorization);
    }

    public void registerAuthorization(String authorization) {
        registerAuthorization(authorizationRuleParser.parseAuthorization(authorization));
    }

    @Override
    public Class operationAuthorizationRequestClass() {
        return Object.class;
    }

    @Override
    public boolean authorizes(Object operationAuthorizationRequest) {
        Object parsedAuthority = operationAuthorizationRequest instanceof String && operationAuthorizationRequestParser != null ? operationAuthorizationRequestParser.parseOperationAuthorizationRequest((String) operationAuthorizationRequest) : operationAuthorizationRequest;
        return Collections.hasAtLeastOneMatching(
                registeredAuthorizations.get(operationAuthorizationRequest.getClass()),
                authorization -> authorization.authorizes(parsedAuthority)
        );
    }
}
