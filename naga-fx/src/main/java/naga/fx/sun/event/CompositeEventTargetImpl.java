package naga.fx.sun.event;

import naga.fx.event.EventTarget;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class CompositeEventTargetImpl implements CompositeEventTarget {
    private final Set<EventTarget> eventTargets;

    CompositeEventTargetImpl(final EventTarget... eventTargets) {
        Set<EventTarget> mutableSet = new HashSet<>(eventTargets.length);
        mutableSet.addAll(Arrays.asList(eventTargets));

        this.eventTargets = Collections.unmodifiableSet(mutableSet);
    }

    @Override
    public Set<EventTarget> getTargets() {
        return eventTargets;
    }

    @Override
    public boolean containsTarget(EventTarget target) {
        return eventTargets.contains(target);
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        EventDispatchTree eventDispatchTree = (EventDispatchTree) tail;

        for (EventTarget eventTarget: eventTargets) {
            EventDispatchTree targetDispatchTree = eventDispatchTree.createTree();
            eventDispatchTree = eventDispatchTree.mergeTree(
                    (EventDispatchTree) eventTarget.buildEventDispatchChain(targetDispatchTree));
        }

        return eventDispatchTree;
    }
}
