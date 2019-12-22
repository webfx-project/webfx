package webfx.framework.shared.orm.expression.parser.lci;

import webfx.framework.shared.orm.expression.terms.Symbol;

/**
 * Loose coupling interface between the parser and the domain model. Each time the parser needs to access the domain
 * model, it does it using this interface. So the parser is not linked to a specific representation of the domain model
 * (any domain model representation should easily provide an implementation of this interface).
 *
 * @author Bruno Salmon
 */
public interface ParserDomainModelReader {

    /**
     * Return the domain class given its name. Ex: when passing "Country" string, it should return the Country domain class.
     *
     * Note: the returned domain class object will be used only to be passed as argument to other methods of this
     * interface. So the implementing class may choose to return the actual domain class object or just an identifier.
     *
     * @param name the domain class name
     * @return the domain class object
     */
    Object getDomainClassByName(String name);

    /**
     * Return the domain field given its name (expressed in form of a Symbol instance).
     *
     * @param domainClass the domain class of the domain field
     * @param fieldName the domain field name
     * @return the domain field expressed in form of a Symbol instance
     */
    Symbol getDomainFieldSymbol(Object domainClass, String fieldName);

    /**
     * Return the field group given its name (expressed in form of a Symbol instance). A field group such as "<default>"
     * (fieldGroupName = "default" in this case) should have a list of fields (ex: firstName, lastName, age). This list
     * of fields can be get by calling the getExpression() method of the returned Symbol instance.
     *
     * @param domainClass the domain class of the field group
     * @param fieldGroupName the field group name
     * @return the field group expressed in form of a Symbol instance
     */
    Symbol getDomainFieldGroupSymbol(Object domainClass, String fieldGroupName);

    /**
     * Return the foreign domain class of a symbol (typically a foreign field) that is supposed to point to another
     * domain class. Ex: if continent is a field of a Country domain class that points to a Continent domain class, this
     * method will return the Continent domain class when passing the continent field.
     *
     * @param symbolDomainClass the domain class of the symbol
     * @param symbol the symbol
     * @return the foreign domain class
     */
    Object getSymbolForeignDomainClass(Object symbolDomainClass, Symbol symbol);

}
