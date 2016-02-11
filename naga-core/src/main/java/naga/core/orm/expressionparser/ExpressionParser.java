package naga.core.orm.expressionparser;

import java_cup.runtime.Symbol;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.ExpressionArray;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionparser.expressionbuilder.BuilderThreadContext;
import naga.core.orm.expressionparser.expressionbuilder.term.ExpressionBuilder;
import naga.core.orm.expressionparser.expressionbuilder.term.SelectBuilder;
import naga.core.orm.expressionparser.javacup.JavaCupParser;
import naga.core.orm.expressionparser.jflex.Lexer;
import naga.core.orm.expressionparser.lci.ParserModelReader;

import java.io.StringReader;

/**
 * @author Bruno Salmon
 */
public class ExpressionParser {

    public static Expression parseExpression(String definition, Object domainClass, ParserModelReader modelReader) {
        return parseExpression(definition, domainClass, modelReader, false);
    }

    public static Expression parseExpression(String definition, Object domainClass, ParserModelReader modelReader, boolean expectList) {
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

    public static Select parseSelect(String definition, ParserModelReader modelReader) {
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

    public static Symbol parseWithJavaCup(String definition) throws Exception {
        JavaCupParser javaCupParser = new JavaCupParser(new Lexer(new StringReader(definition)));
        return javaCupParser.parse();
    }

}
