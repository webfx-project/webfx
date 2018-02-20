package naga.framework.spi.authz.impl.inmemory;

/**
 * @author Bruno Salmon
 */
public enum AuthorizationRuleResult {
    GRANTED,
    DENIED,
    OUT_OF_RULE_CONTEXT
}
