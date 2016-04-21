package naga.core.orm.domainmodel;

import naga.core.orm.domainmodel.lciimpl.CompilerDomainModelReaderImpl;
import naga.core.orm.domainmodel.lciimpl.ParserDomainModelReaderImpl;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.ExpressionArray;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionsqlcompiler.ExpressionSqlCompiler;
import naga.core.orm.expressionsqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainModel {
    private final Object id;
    private final Map<Object /* id, modelId, name or sqlTable */, DomainClass> classMap;

    public DomainModel(Object id, Map<Object, DomainClass> classMap) {
        this.id = id;
        this.classMap = classMap;
    }

    public Object getId() {
        return id;
    }

    public DomainClass getClass(Object classId) {
        /* FROM KBS 2.0 if (classId instanceof ID)
            classId = ((ID) classId).getObjId(); */
        return classMap.get(classId);
    }

    public Expression parseExpression(String definition, Object classId) {
        return ExpressionParser.parseExpression(definition, classId, new ParserDomainModelReaderImpl(this));
    }

    public ExpressionArray parseExpressionArray(String definition, Object classId) {
        return ExpressionParser.parseExpressionArray(definition, classId, new ParserDomainModelReaderImpl(this));
    }

    public Select parseSelect(String definition) {
        return ExpressionParser.parseSelect(definition, new ParserDomainModelReaderImpl(this));
    }

    public SqlCompiled compileSelect(String stringSelect) {
        return compileSelect(stringSelect, null);
    }

    public SqlCompiled compileSelect(String stringSelect, Object[] parameterValues) {
        return compileSelect(parseSelect(stringSelect), parameterValues);
    }

    public SqlCompiled compileSelect(Select select) {
        return compileSelect(select, null);
    }

    public SqlCompiled compileSelect(Select select, Object[] parameterValues) {
        return ExpressionSqlCompiler.compileSelect(select, parameterValues, DbmsSqlSyntaxOptions.POSTGRES_SYNTAX, true, true, CompilerDomainModelReaderImpl.SINGLETON);
    }

    public static String toSqlString(String logicalName) {
        if (logicalName == null || logicalName.length() < 2)
            return logicalName;
        StringBuilder sb = new StringBuilder();
        boolean underscoreAllowed = false;
        int i0 = 0, i = 1;
        for (; i < logicalName.length(); i++) {
            if (Character.isUpperCase(logicalName.charAt(i))) {
                if (underscoreAllowed)
                    sb.append('_');
                for (int j = i0; j < i; j++)
                    sb.append(Character.toLowerCase(logicalName.charAt(j)));
                underscoreAllowed = i > i0 + 1; // no underscore between two consecutive uppercase
                i0 = i;
            }
        }
        if (underscoreAllowed)
            sb.append('_');
        for (int j = i0; j < i; j++)
            sb.append(Character.toLowerCase(logicalName.charAt(j)));
        return sb.toString();
    }

}
