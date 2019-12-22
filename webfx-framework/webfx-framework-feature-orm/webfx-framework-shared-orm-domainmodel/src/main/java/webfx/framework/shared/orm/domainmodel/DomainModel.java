package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.domainmodel.lciimpl.ParserDomainModelReaderImpl;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.parser.ExpressionParser;
import webfx.framework.shared.orm.expression.terms.DqlStatement;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.expression.terms.Select;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class DomainModel {

    private final Object id;
    private final Map<Object /* id, modelId, name or sqlTable */, DomainClass> classMap;
    private final ParserDomainModelReaderImpl parserDomainModelReader = new ParserDomainModelReaderImpl(this);

    public DomainModel(Object id, Map<Object, DomainClass> classMap) {
        this.id = id;
        this.classMap = classMap;
    }

    public Object getId() {
        return id;
    }

    public DomainClass getClass(Object classId) {
        return classMap.get(classId);
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

    public <T> DqlStatement<T> parseStatement(String definition) {
        return ExpressionParser.parseStatement(definition, parserDomainModelReader);
    }
}
