package naga.core.orm.expressionparser.expressionbuilder;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ReferenceResolver {

    Expression resolveReference(String name); // returns Alias or Field

}
