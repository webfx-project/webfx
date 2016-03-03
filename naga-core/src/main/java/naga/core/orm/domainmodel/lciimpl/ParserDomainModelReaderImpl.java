package naga.core.orm.domainmodel.lciimpl;

import naga.core.orm.domainmodel.DomainClass;
import naga.core.orm.domainmodel.DomainField;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.expression.term.Symbol;
import naga.core.orm.expressionparser.lci.ParserDomainModelReader;

/**
 * @author Bruno Salmon
 */
public class ParserDomainModelReaderImpl implements ParserDomainModelReader {

    private final DomainModel domainModel;

    public ParserDomainModelReaderImpl(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    @Override
    public Object getDomainClassByName(String name) {
        return domainModel.getClass(name);
    }

    private DomainClass toDomainClass(Object domainClass) {
        if (domainClass instanceof DomainClass)
            return (DomainClass) domainClass;
        return domainModel.getClass(domainClass);
    }

    @Override
    public Symbol getDomainFieldSymbol(Object domainClass, String fieldName) {
        return toDomainClass(domainClass).getField(fieldName);
    }

    @Override
    public Symbol getDomainFieldGroupSymbol(Object domainClass, String fieldGroupName) {
        return toDomainClass(domainClass).getFieldsGroup(fieldGroupName);
    }

    @Override
    public Object getSymbolForeignDomainClass(Object symbolDomainClass, Symbol symbol) {
        if (symbol instanceof DomainField)
            return ((DomainField) symbol).getForeignClass();
        return null;
    }
}
