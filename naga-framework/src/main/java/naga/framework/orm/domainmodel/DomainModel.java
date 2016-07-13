package naga.framework.orm.domainmodel;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.CompilerDomainModelReader;
import naga.framework.expression.parser.ExpressionParser;
import naga.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import naga.framework.expression.sqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Select;
import naga.framework.orm.domainmodel.lciimpl.CompilerDomainModelReaderImpl;
import naga.framework.orm.domainmodel.lciimpl.ParserDomainModelReaderImpl;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainModel {
    private final Object id;
    private final Map<Object /* id, modelId, name or sqlTable */, DomainClass> classMap;
    private final CompilerDomainModelReader compilerDomainModelReader = new CompilerDomainModelReaderImpl(this);
    private final ParserDomainModelReaderImpl parserDomainModelReader = new ParserDomainModelReaderImpl(this);

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

    public CompilerDomainModelReader getCompilerDomainModelReader() {
        return compilerDomainModelReader;
    }

    public ParserDomainModelReaderImpl getParserDomainModelReader() {
        return parserDomainModelReader;
    }

    public Expression parseExpression(String definition, Object classId) {
        return ExpressionParser.parseExpression(definition, classId, parserDomainModelReader);
    }

    public ExpressionArray parseExpressionArray(String definition, Object classId) {
        return ExpressionParser.parseExpressionArray(definition, classId, parserDomainModelReader);
    }

    public Select parseSelect(String definition) {
        return ExpressionParser.parseSelect(definition, parserDomainModelReader);
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
        return ExpressionSqlCompiler.compileSelect(select, parameterValues, DbmsSqlSyntaxOptions.POSTGRES_SYNTAX, true, true, compilerDomainModelReader);
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
