package webfx.framework.shared.orm.expression.terms.function;

import java.util.Stack;

/**
 * @author Bruno Salmon
 */
final class ThreadLocalArgumentStack {

    private static final ThreadLocal<Stack> stacks = new ThreadLocal<>();

    private static Stack getThreadLocalArgumentStack() {
        Stack stack = stacks.get();
        if (stack == null)
            stacks.set(stack = new Stack<>());
        return stack;
    }

    public static void pushArgument(Object argument) {
        getThreadLocalArgumentStack().push(argument);
    }

    public static void popArgument() {
        getThreadLocalArgumentStack().pop();
    }

    public static Object getArgument(int index) {
        Stack stack = getThreadLocalArgumentStack();
        if (!stack.isEmpty()) {
            Object argument = stack.peek();
            if (argument instanceof Object[])
                return ((Object[]) argument)[index];
            else if (index == 0)
                return argument;
        }
        return new IllegalArgumentException();
    }

}
