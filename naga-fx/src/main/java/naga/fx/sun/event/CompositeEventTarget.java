package naga.fx.sun.event;

import naga.fx.event.EventTarget;

import java.util.Set;

interface CompositeEventTarget extends EventTarget {

    Set<EventTarget> getTargets();

    boolean containsTarget(EventTarget target);
}

