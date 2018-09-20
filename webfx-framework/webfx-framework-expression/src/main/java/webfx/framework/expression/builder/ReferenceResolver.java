package webfx.framework.expression.builder;

import webfx.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ReferenceResolver {

    Expression resolveReference(String name); // returns Alias or Field

}
