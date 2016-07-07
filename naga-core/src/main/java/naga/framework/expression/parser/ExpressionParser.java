package naga.framework.expression.parser;

import java_cup.runtime.Symbol;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Select;
import naga.framework.expression.builder.BuilderThreadContext;
import naga.framework.expression.builder.terms.ExpressionBuilder;
import naga.framework.expression.builder.terms.SelectBuilder;
import naga.framework.expression.parser.javacup.JavaCupExpressionParser;
import naga.framework.expression.parser.jflex.ExpressionLexer;
import naga.framework.expression.lci.ParserDomainModelReader;

import java.io.StringReader;

/**
 * @author Bruno Salmon
 */
public class ExpressionParser {

    public static Expression parseExpression(String definition, Object domainClass, ParserDomainModelReader modelReader) {
        return parseExpression(definition, domainClass, modelReader, false);
    }

    public static ExpressionArray parseExpressionArray(String definition, Object domainClass, ParserDomainModelReader modelReader) {
        return (ExpressionArray) parseExpression(definition, domainClass, modelReader, true);
    }

    public static Expression parseExpression(String definition, Object domainClass, ParserDomainModelReader modelReader, boolean expectList) {
        try (BuilderThreadContext context = BuilderThreadContext.open(modelReader)) {
            Symbol symbol = parseWithJavaCup("expr:=" + definition);
            ExpressionBuilder expressionBuilder = (ExpressionBuilder) symbol.value;
            expressionBuilder.buildingClass = domainClass;
            Expression expression = expressionBuilder.build();
            if (!expectList && expression instanceof ExpressionArray) {
                Expression[] expressions = ((ExpressionArray) expression).getExpressions();
                if (expressions.length == 1)
                    expression = expressions[0];
            }
            return expression;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Select parseSelect(String definition, ParserDomainModelReader modelReader) {
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
