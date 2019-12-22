package webfx.framework.shared.orm.expression.terms;

/**
 * Interface to mark symbols that are linked to a domain class (ex: domain field). This information is used by the
 * SymbolSqlCompiler to understand the context of a symbol when not prefixed with an alias (in this case the compiler
 * needs to know if the symbol refers to the current class/table or to a upper level when sub-queries are used)
 */
public interface HasDomainClass {

    Object getDomainClass();

}
