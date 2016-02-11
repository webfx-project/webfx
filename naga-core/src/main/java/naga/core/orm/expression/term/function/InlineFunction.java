package naga.core.orm.expression.term.function;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.orm.expression.term.ExpressionArray;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionparser.expressionbuilder.ReferenceResolver;
import naga.core.orm.expressionparser.expressionbuilder.ThreadLocalReferenceResolver;
import naga.core.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class InlineFunction<T> extends Function<T> {

    private final Expression body;

    public InlineFunction(String name, String signature, Type[] argTypes, String body) {
        this(name, signature.split(","), argTypes, body);
    }

    public InlineFunction(String name, String[] argNames, Type[] argTypes, String body) {
        this(name, argNames, argTypes, parseBody(body, argNames, argTypes));
    }

    public InlineFunction(String name, String[] argNames, Type[] argTypes, Expression body) {
        super(name, argNames, argTypes, body.getType(), true);
        this.body = body;
    }

    public Expression getBody() {
        return body;
    }

    @Override
    public Object evaluate(Object argument, DataReader dataReader) {
        try {
            pushArguments(argument);
            return body.evaluate(null, dataReader);
        } finally {
            popArguments();
        }
    }

    @Override
    public int getPrecedenceLevel() {
        return body.getPrecedenceLevel();
    }

    private static Expression parseBody(String body, final String[] argNames, final Type[] argTypes) {
        try {
            ThreadLocalReferenceResolver.pushReferenceResolver(new ReferenceResolver() {
                Map<String, ArgumentAlias> argumentAliases = new HashMap<>();
                @Override
                public Expression resolveReference(String name) {
                    ArgumentAlias argumentAlias = argumentAliases.get(name);
                    if (argumentAlias != null) // in case it was already resolved, no need to create a new instance, we reuse the existing one
                        return argumentAlias;
                    for (int i = 0; i < argNames.length; i++) {
                        if (name.equals(argNames[i])) {
                            argumentAliases.put(name, argumentAlias = new ArgumentAlias(name, argTypes == null ? null : argTypes[i], i));
                            return argumentAlias;
                        }
                    }
                    return null;
                }
            });
            return ExpressionParser.parseExpression(body, null, null, false);
        } finally {
            ThreadLocalReferenceResolver.popReferenceResolver();
        }

    }

    public void pushArguments(Object argument) { // single argument or array of arguments
        if (argument instanceof ExpressionArray)
            argument = ((ExpressionArray) argument).getExpressions();
        ThreadLocalArgumentStack.pushArgument(argument);
    }

    public void popArguments() {
        ThreadLocalArgumentStack.popArgument();
    }

}
