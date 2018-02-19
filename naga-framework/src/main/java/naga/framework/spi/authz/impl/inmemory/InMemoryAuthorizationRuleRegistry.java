package naga.framework.spi.authz.impl.inmemory;

import naga.framework.spi.authz.impl.inmemory.parser.InMemoryAuthorizationRuleParser;
import naga.framework.spi.authz.impl.inmemory.parser.InMemoryAuthorizationRuleParserRegistry;
import naga.framework.spi.authz.impl.inmemory.parser.OperationAuthorizationRequestParser;
import naga.framework.spi.authz.impl.inmemory.parser.OperationAuthorizationRequestParserRegistry;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class InMemoryAuthorizationRuleRegistry implements InMemoryAuthorizationRule {

    private final Map<Class, Collection<InMemoryAuthorizationRule>> registeredInMemoryAuthorizationRules = new HashMap<>();
    private InMemoryAuthorizationRuleParser inMemoryAuthorizationRuleParser;
    private OperationAuthorizationRequestParser operationAuthorizationRequestParser;

    public void setInMemoryAuthorizationRuleParser(InMemoryAuthorizationRuleParser ruleParser) {
        this.inMemoryAuthorizationRuleParser = ruleParser;
    }

    public void addInMemoryAuthorizationRuleParser(InMemoryAuthorizationRuleParser ruleParser) {
        if (inMemoryAuthorizationRuleParser == null)
            setInMemoryAuthorizationRuleParser(ruleParser);
        else {
            InMemoryAuthorizationRuleParserRegistry registry;
            if (inMemoryAuthorizationRuleParser instanceof InMemoryAuthorizationRuleParserRegistry)
                registry = (InMemoryAuthorizationRuleParserRegistry) inMemoryAuthorizationRuleParser;
            else {
                registry = new InMemoryAuthorizationRuleParserRegistry();
                registry.registerParser(inMemoryAuthorizationRuleParser);
            }
            registry.registerParser(ruleParser);
        }
    }

    public void setOperationAuthorizationRequestParser(OperationAuthorizationRequestParser requestParser) {
        this.operationAuthorizationRequestParser = requestParser;
    }

    public void addOperationAuthorizationRequestParser(OperationAuthorizationRequestParser requestParser) {
        if (operationAuthorizationRequestParser == null)
            setOperationAuthorizationRequestParser(requestParser);
        else {
            OperationAuthorizationRequestParserRegistry registry;
            if (operationAuthorizationRequestParser instanceof InMemoryAuthorizationRuleParserRegistry)
                registry = (OperationAuthorizationRequestParserRegistry) operationAuthorizationRequestParser;
            else {
                registry = new OperationAuthorizationRequestParserRegistry();
                registry.registerParser(operationAuthorizationRequestParser);
            }
            registry.registerParser(requestParser);
        }
    }

    public <A> void registerInMemoryAuthorizationRule(Class<A> requiredAuthorizationClass, InMemoryAuthorizationRule<A> authorizationRule) {
        Collection<InMemoryAuthorizationRule> inMemoryAuthorizationRules = registeredInMemoryAuthorizationRules.get(requiredAuthorizationClass);
        if (inMemoryAuthorizationRules == null)
            registeredInMemoryAuthorizationRules.put(requiredAuthorizationClass, inMemoryAuthorizationRules = new ArrayList<>());
        inMemoryAuthorizationRules.add(authorizationRule);
    }

    public <A> void registerInMemoryAuthorizationRule(InMemoryAuthorizationRule inMemoryAuthorizationRule) {
        registerInMemoryAuthorizationRule(inMemoryAuthorizationRule.operationAuthorizationRequestClass(), inMemoryAuthorizationRule);
    }

    public void registerInMemoryAuthorizationRule(String authorization) {
        registerInMemoryAuthorizationRule(inMemoryAuthorizationRuleParser.parseAuthorization(authorization));
    }

    @Override
    public Class operationAuthorizationRequestClass() {
        return Object.class;
    }

    @Override
    public boolean authorizes(Object operationAuthorizationRequest) {
        Object parsedOperationAuthorizationRequest = operationAuthorizationRequest instanceof String && operationAuthorizationRequestParser != null ? operationAuthorizationRequestParser.parseOperationAuthorizationRequest((String) operationAuthorizationRequest) : operationAuthorizationRequest;
        return Collections.hasAtLeastOneMatching(
                registeredInMemoryAuthorizationRules.get(operationAuthorizationRequest.getClass()),
                authorization -> authorization.authorizes(parsedOperationAuthorizationRequest)
        );
    }
}
