package naga.core.orm.expressionsqlcompiler.lci;

import naga.core.orm.expression.Expression;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionparser.lci.ParserDomainModelReaderMock;
import naga.core.orm.expressionsqlcompiler.ExpressionSqlCompiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Bruno Salmon
 */
public class CompilerDomainModelReaderMock implements CompilerDomainModelReader {

    private final Map<String, Expression> classForeignFields = new HashMap<>();
    private final Set<String> foreignFields = new HashSet<>(); // ClassName.fieldName

    public CompilerDomainModelReaderMock declareAsForeignField(Object domainClass, String fieldName) {
        foreignFields.add(getKey(domainClass, fieldName));
        return this; // fluent API
    }

    public CompilerDomainModelReaderMock setDomainClassForeignFields(Object domainClass, String foreignFieldsDefinition) {
        classForeignFields.put(getClassName(domainClass), ExpressionParser.parseExpression(foreignFieldsDefinition, domainClass, new ParserDomainModelReaderMock()));
        return this; // fluent API
    }

    private String getKey(Object domainClass, String fieldName) {
        return getClassName(domainClass) + '.' + fieldName;
    }

    private String getClassName(Object domainClass) {
        return domainClass.toString();
    }

    @Override
    public String getDomainClassSqlTableName(Object domainClass) {
        return ExpressionSqlCompiler.toSqlString(getClassName(domainClass));
    }

    @Override
    public String getDomainClassPrimaryKeySqlColumnName(Object domainClass) {
        return "id";
    }

    @Override
    public String getSymbolSqlColumnName(Object symbolDomainClass, Expression symbol) {
        String columnName = ExpressionSqlCompiler.toSqlString(symbol.toString());
        if (isForeignTerm(symbolDomainClass, symbol, false))
            columnName += "_id";
        return columnName;
    }

    private boolean isForeignTerm(Object termDomainClass, Expression term, boolean required) {
        String key = getKey(termDomainClass, term.toString());
        if (foreignFields.contains(key))
            return true;
        if (!required)
            return false;
        foreignFields.add(key);
        return true;
    }

    @Override
    public Object getSymbolForeignDomainClass(Object symbolDomainClass, Expression symbol, boolean required) {
        if (!isForeignTerm(symbolDomainClass, symbol, required))
            return null;
        String name = symbol.toString();  // term name, ex: "event" (in a dot navigation like event.name)
        return Character.toUpperCase(name.charAt(0)) + name.substring(1); // return "Event" as the foreign class
    }

    @Override
    public Expression getDomainClassDefaultForeignFields(Object domainClass) {
        return classForeignFields.get(getClassName(domainClass));
    }

}
