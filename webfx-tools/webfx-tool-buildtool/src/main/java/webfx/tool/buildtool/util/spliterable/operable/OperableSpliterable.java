package webfx.tool.buildtool.util.spliterable.operable;

import webfx.tool.buildtool.util.spliterable.Spliterable;
import webfx.tool.buildtool.util.spliterable.ThrowableSpliterable;
import webfx.tool.buildtool.util.spliterable.operable.impl.OperableSpliterableImpl;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public interface OperableSpliterable<T> extends Spliterable<T> {

    OperableSpliterable<T> filter(Predicate<? super T> predicate);

    <R> OperableSpliterable<R> map(Function<? super T, ? extends R> mapper);

    <R> OperableSpliterable<R> flatMap(Function<? super T, ? extends Spliterable<? extends R>> mapper);

    OperableSpliterable<T> cache();

    static <T> OperableSpliterable<T> create(ThrowableSpliterable<T> builder) {
        return new OperableSpliterableImpl<>(builder);
    }
}
