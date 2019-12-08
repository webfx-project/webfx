package webfx.framework.shared.orm.expression.parser.lci.mock;

import webfx.framework.shared.orm.expression.parser.lci.ParserDomainModelReader;
import webfx.framework.shared.orm.expression.terms.Symbol;
import webfx.framework.shared.orm.expression.parser.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class ParserDomainModelReaderMock implements ParserDomainModelReader {

    private final Map<String, String> fieldGroups = new HashMap<>();

    public ParserDomainModelReaderMock setFieldGroup(String name, String definition) {
        fieldGroups.put(name, definition);
        return this; // fluent API
    }

    @Override
    public Object getDomainClassByName(String name) {
        return name; // Domain classes are just names in this dummy model (no existence check and no additional info about them)
    }

    @Override
    public Symbol getDomainFieldSymbol(Object domainClass, String fieldName) {
        return new Symbol(fieldName); // a field is expected ? ok no pb, we deliver it (no existence check)
    }

    @Override
    public Symbol getDomainFieldGroupSymbol(Object domainClass, String fieldGroupName) {
        String fieldGroupDefinition = fieldGroups.get(fieldGroupName);
        if (fieldGroupDefinition != null)
            return new Symbol('<' + fieldGroupName + '>', ExpressionParser.parseExpression(fieldGroupDefinition, domainClass, this));
        return null;
    }

    @Override
    public Object getSymbolForeignDomainClass(Object symbolDomainClass, Symbol symbol) {
        String name = symbol.getName(); // term name, ex: "event" (in a dot navigation like event.name)
        return Character.toUpperCase(name.charAt(0)) + name.substring(1); // return "Event" as the foreign class
    }
}
