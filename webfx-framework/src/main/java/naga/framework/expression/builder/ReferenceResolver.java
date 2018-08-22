package naga.framework.expression.builder;

import naga.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ReferenceResolver {

    Expression resolveReference(String name); // returns Alias or Field

}
