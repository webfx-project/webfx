package webfx.framework.expression.builder;

import java.util.Stack;
import webfx.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class ThreadLocalReferenceResolver {

    private static final ThreadLocal<Stack<ReferenceResolver>> stack = new ThreadLocal<>();

    private static Stack<ReferenceResolver> getThreadLocalResolvers() {
        Stack<ReferenceResolver> referenceResolvers = stack.get();
        if (referenceResolvers == null)
            stack.set(referenceResolvers = new Stack<>());
        return referenceResolvers;
    }

    public static void pushReferenceResolver(ReferenceResolver referenceResolver) {
        getThreadLocalResolvers().push(referenceResolver);
    }

    public static void popReferenceResolver() {
        getThreadLocalResolvers().pop();
    }

    public static Expression resolveReference(String name) {
        Stack<ReferenceResolver> resolvers = getThreadLocalResolvers();
        for (int i = resolvers.size() - 1; i >= 0; i--) {
            Expression e = resolvers.get(i).resolveReference(name);
            if (e != null)
                return e;
        }
        return null;
    }

}
