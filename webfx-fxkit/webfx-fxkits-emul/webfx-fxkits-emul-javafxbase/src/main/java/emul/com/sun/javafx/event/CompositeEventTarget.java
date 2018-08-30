/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package emul.com.sun.javafx.event;

import java.util.Set;
import emul.javafx.event.EventTarget;

public interface CompositeEventTarget extends EventTarget {
    Set<EventTarget> getTargets();

    boolean containsTarget(EventTarget target);
}
