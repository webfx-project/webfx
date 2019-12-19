package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
final class TakeWhileOperator<T> extends Operator<T, T> {

    private final Predicate<? super T> predicate;

    TakeWhileOperator(Spliterable<T> wrappedSpliterable, Predicate<? super T> predicate) {
        super(wrappedSpliterable);
        this.predicate = predicate;
    }

    @Override
    TakeWhileOperation<T> newOperation() {
        return new TakeWhileOperation<>();
    }

    final class TakeWhileOperation<_T extends T> extends Operation<_T, _T> {

        private _T rejectedElement;

        @Override
        Consumer<? super _T> createWrappedAction(Consumer<? super _T> action) {
            return t -> {
                if (predicate.test(t))
                    action.accept(t);
                else {
                    rejectedElement = t;
                    Spliterator<_T> wrappedSpliterator = getWrappedSpliterator();
                    if (wrappedSpliterator instanceof PushBackSpliterator)
                        ((PushBackSpliterator<T, ?>) wrappedSpliterator).pushBackLastElement();
                }
            };
        }

        @Override
        public boolean tryAdvance(Consumer<? super _T> action) {
            if (rejectedElement != null)
                onWrappedSpliteratorFullyTraversed();
            else if (!super.tryAdvance(action))
                return false;
            else if (rejectedElement != null)
                onWrappedSpliteratorFullyTraversed();
            return rejectedElement == null;
        }

        @Override
        public void forEachRemaining(Consumer<? super _T> action) {
            if (!hasWrappedSpliteratorBeenFullyTraversed()) {
                //noinspection StatementWithEmptyBody
                while (tryAdvance(action)) ;
                onWrappedSpliteratorFullyTraversed();
            }
        }
    }
}
