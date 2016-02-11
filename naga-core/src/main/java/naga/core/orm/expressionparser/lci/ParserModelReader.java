package naga.core.orm.expressionparser.lci;

import naga.core.orm.expression.term.Symbol;

/**
 * @author Bruno Salmon
 */
public interface ParserModelReader {

    Object getDomainClass(String name);

    Symbol getDomainFieldSymbol(Object domainClass, String fieldName);

    Symbol getDomainFieldGroupSymbol(Object domainClass, String fieldGroupName);

    Object getSymbolForeignDomainClass(Object symbolDomainClass, Symbol symbol);

}
