package webfx.framework.shared.orm.expression.builder;

import webfx.framework.shared.orm.expression.Expression;

import java.util.Stack;

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

    public static void executeCodeInvolvingReferenceResolver(Runnable code, ReferenceResolver referenceResolver) {
        try {
            // Before calling the code, we push the reference resolver in the thread context
            ThreadLocalReferenceResolver.pushReferenceResolver(referenceResolver);
            // Now that the ReferenceResolver is set, we can call the code
            code.run();
        } finally {
            // Now that the code is executed, we can pop the reference resolver
            ThreadLocalReferenceResolver.popReferenceResolver();
        }
    }
}
