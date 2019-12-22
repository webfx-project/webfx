package webfx.framework.shared.orm.dql.sqlcompiler.lci;

import webfx.framework.shared.orm.expression.Expression;

/**
 * Loose coupling interface between the compiler and the domain model. Each time the compiler needs to access the domain
 * model, it does it using this interface. So the compiler is not linked to a specific representation of the domain model
 * (any domain model representation should easily provide an implementation of this interface).
 *
 * @author Bruno Salmon
 */
public interface CompilerDomainModelReader {

    /**
     * Return the sql table name of a given domain class.
     *
     * Note: the domain class object is actually coming from the ParserDomainModelReader.getDomainClassByName() method
     * and it will be used only to be passed as argument to other methods of this interface. So depending of the
     * implementation of the ParserDomainModelReader, it may be the actual domain class object or just an identifier.
     *
     * @param domainClass the domain class
     * @return the sql table name of the domain class
     */
    String getDomainClassSqlTableName(Object domainClass);

    /**
     * Return the sql column name of the primary key of a given domain class
     *
     * @param domainClass the domain class
     * @return the sql column name of the primary key
     */
    String getDomainClassPrimaryKeySqlColumnName(Object domainClass);

    /**
     * Return the sql column name of an expression that is a symbol (typically a field) of a given domain class.
     *
     * @param symbolDomainClass the domain class of the symbol
     * @param symbol the symbol in the form of an expression
     * @return the column name
     */
    String getSymbolSqlColumnName(Object symbolDomainClass, Expression symbol);

    /**
     * Return the foreign domain class of a symbol (typically a foreign field) that is supposed to point to another
     * domain class. Ex: if continent is a field of a Country domain class that points to a Continent domain class, this
     * method will return the Continent domain class when passing the continent field.
     *
     * @param symbolDomainClass the domain class of the symbol
     * @param symbol the symbol in the form of an expression
     * @param required if true, this method can't return null otherwise it will produce a sql compilation error
     * @return the foreign domain class
     */
    Object getSymbolForeignDomainClass(Object symbolDomainClass, Expression symbol, boolean required);

    /**
     * Return the default fields to read when a foreign field is listed in the select without the explicit final fields.
     * Ex: when compiling "select name,continent from Country", reading the continent foreign field may mean to read the
     * continent name (if name is the default). In this case this method should return the name field when invoked with
     * the Continent domain class. If this method returns null, only the primary key of the foreign field will be read.
     *
     * @param domainClass the domain class
     * @return the actual default fields to read when reading a foreign field of this domain class
     */
    Expression getDomainClassDefaultForeignFields(Object domainClass);

}
