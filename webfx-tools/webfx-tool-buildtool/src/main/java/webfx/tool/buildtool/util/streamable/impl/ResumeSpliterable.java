package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.Spliterator;

/**
 * @author Bruno Salmon
 */
final class ResumeSpliterable<T> implements Spliterable<T> {

    private Spliterable<T> spliteratorFactory;
    private Spliterator<T> spliterator;

    ResumeSpliterable(Spliterable<T> spliteratorFactory) {
        this.spliteratorFactory = spliteratorFactory;
    }

    @Override
    public Spliterator<T> spliterator() {
        if (spliterator == null && spliteratorFactory != null) {
            spliterator = spliteratorFactory.spliterator();
            spliteratorFactory = null;
        }
        return spliterator;
    }
}
