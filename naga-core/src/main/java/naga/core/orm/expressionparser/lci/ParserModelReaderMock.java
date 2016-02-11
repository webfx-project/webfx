package naga.core.orm.expressionparser.lci;

import naga.core.orm.expression.term.Symbol;
import naga.core.orm.expressionparser.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ParserModelReaderMock implements ParserModelReader {

    private final Map<String, String> fieldGroups = new HashMap<>();

    public ParserModelReaderMock setFieldGroup(String name, String definition) {
        fieldGroups.put(name, definition);
        return this; // fluent API
    }

    @Override
    public Object getDomainClass(String name) {
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
