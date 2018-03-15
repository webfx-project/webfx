package naga.framework.orm.domainmodel;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.CompilerDomainModelReader;
import naga.framework.expression.parser.ExpressionParser;
import naga.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.expression.sqlcompiler.sql.dbms.PostgresSyntax;
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

    public <T> Expression<T> parseExpression(String definition, Object classId) {
        return ExpressionParser.parseExpression(definition, classId, parserDomainModelReader);
    }

    public <T> ExpressionArray<T> parseExpressionArray(String definition, Object classId) {
        return ExpressionParser.parseExpressionArray(definition, classId, parserDomainModelReader);
    }

    public <T> Select<T> parseSelect(String definition) {
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
        return ExpressionSqlCompiler.compileSelect(select, parameterValues, PostgresSyntax.get(), true, true, compilerDomainModelReader);
    }
}
