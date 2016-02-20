package naga.core.orm.domainmodel.lciimpl;

import naga.core.orm.domainmodel.DomainClass;
import naga.core.orm.domainmodel.DomainField;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.IDExpression;
import naga.core.orm.expressionsqlcompiler.lci.CompilerDomainModelReader;

/**
 * @author Bruno Salmon
 */
public class CompilerDomainModelReaderImpl implements CompilerDomainModelReader {

    public static CompilerDomainModelReaderImpl SINGLETON = new CompilerDomainModelReaderImpl();

    private CompilerDomainModelReaderImpl() {
    }

    private DomainClass getDomainClass(Object domainClass) {
        return (DomainClass) domainClass;
    }

    @Override
    public String getDomainClassSqlTableName(Object domainClass) {
        return getDomainClass(domainClass).getSqlTableName();
    }

    @Override
    public String getDomainClassPrimaryKeySqlColumnName(Object domainClass) {
        return getDomainClass(domainClass).getIdColumnName();
    }

    @Override
    public Object getSymbolForeignDomainClass(Object symbolDomainClass, Expression symbol, boolean required) {
        if (symbol instanceof DomainField)
            return ((DomainField) symbol).getForeignClass();
        return null;
    }

    @Override
    public Expression getDomainClassDefaultForeignFields(Object domainClass) {
        return getDomainClass(domainClass).getForeignFields();
    }

    @Override
    public String getSymbolSqlColumnName(Object symbolDomainClass, Expression symbol) {
        if (symbol instanceof DomainField)
            return ((DomainField) symbol).getSqlColumnName();
        if (symbol instanceof IDExpression)
            return getDomainClassPrimaryKeySqlColumnName(symbolDomainClass);
        return null;
    }
}
