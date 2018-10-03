package webfx.framework.shared.expression.builder;

import webfx.framework.shared.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ReferenceResolver {

    Expression resolveReference(String name); // returns Alias or Field

}
