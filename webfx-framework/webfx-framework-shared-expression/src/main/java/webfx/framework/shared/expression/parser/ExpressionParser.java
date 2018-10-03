package webfx.framework.shared.expression.parser;

import java_cup.runtime.Symbol;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.ExpressionArray;
import webfx.framework.shared.expression.terms.Select;
import webfx.framework.shared.expression.builder.BuilderThreadContext;
import webfx.framework.shared.expression.builder.terms.ExpressionBuilder;
import webfx.framework.shared.expression.builder.terms.SelectBuilder;
import webfx.framework.shared.expression.parser.javacup.JavaCupExpressionParser;
import webfx.framework.shared.expression.parser.jflex.ExpressionLexer;
import webfx.framework.shared.expression.lci.ParserDomainModelReader;

import java.io.StringReader;

/**
 * @author Bruno Salmon
 */
public final class ExpressionParser {

    public static <E> Expression<E> parseExpression(String definition, Object domainClass, ParserDomainModelReader modelReader) {
        return parseExpression(definition, domainClass, modelReader, false);
    }

    public static <E> ExpressionArray<E> parseExpressionArray(String definition, Object domainClass, ParserDomainModelReader modelReader) {
        return (ExpressionArray<E>) parseExpression(definition, domainClass, modelReader, true);
    }

    public static <E> Expression<E> parseExpression(String definition, Object domainClass, ParserDomainModelReader modelReader, boolean expectList) {
        try (BuilderThreadContext context = BuilderThreadContext.open(modelReader)) {
            if (!definition.startsWith("expr:=") && !definition.startsWith("order by "))
                definition = "expr:=" + definition;
            Symbol symbol = parseWithJavaCup(definition);
            ExpressionBuilder expressionBuilder = (ExpressionBuilder) symbol.value;
            expressionBuilder.buildingClass = domainClass;
            Expression<E> expression = expressionBuilder.build();
            if (!expectList && expression instanceof ExpressionArray) {
                Expression<E>[] expressions = ((ExpressionArray<E>) expression).getExpressions();
                if (expressions.length == 1)
                    expression = expressions[0];
            }
            return expression;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <E> Select<E> parseSelect(String definition, ParserDomainModelReader modelReader) {
        try (BuilderThreadContext context = BuilderThreadContext.open(modelReader)) {
            java_cup.runtime.Symbol symbol = parseWithJavaCup(definition);
            SelectBuilder selectBuilder = (SelectBuilder) symbol.value;
            selectBuilder.definition = definition;
            return selectBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Symbol parseWithJavaCup(String definition) throws Exception {
        JavaCupExpressionParser javaCupExpressionParser = new JavaCupExpressionParser(new ExpressionLexer(new StringReader(definition)));
        return javaCupExpressionParser.parse();
    }
}
