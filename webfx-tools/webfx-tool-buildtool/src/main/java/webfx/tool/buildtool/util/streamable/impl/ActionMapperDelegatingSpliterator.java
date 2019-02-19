package webfx.tool.buildtool.util.streamable.impl;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
abstract class ActionMapperDelegatingSpliterator<T> extends DelegatingSpliterator<T> {

    private final Function<Consumer<? super T>, Consumer<? super T>> actionMapper;
    private Consumer<? super T> lastAction, lastMappedAction; // mapped action cache (to prevent recreating it on each getMappedAction() call)

    ActionMapperDelegatingSpliterator(Spliterator<T> delegateSpliterator, Function<Consumer<? super T>, Consumer<? super T>> actionMapper) {
        super(delegateSpliterator);
        this.actionMapper = actionMapper;
    }

    ActionMapperDelegatingSpliterator(Spliterator<T> delegate, Function<Consumer<? super T>, Consumer<? super T>> actionMapper, boolean unsized) {
        super(delegate, unsized);
        this.actionMapper = actionMapper;
    }

    protected Consumer<? super T> getMappedAction(Consumer<? super T> action) {
        return action == lastAction ? lastMappedAction : (lastMappedAction = actionMapper.apply(lastAction = action));
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return super.tryAdvance(getMappedAction(action));
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        super.forEachRemaining(getMappedAction(action));
    }
}
