package com.sun.javafx.event;

import javafx.event.Event;

import java.util.ArrayDeque;
import java.util.Queue;

public final class EventQueue {
    private Queue<Event> queue = new ArrayDeque<Event>();
    private boolean inLoop;

    public void postEvent(Event event) {
        queue.add(event);
    }

    public void fire() {
        if (inLoop) {
            return; //Let the most outer loop do the job
        }
        inLoop = true;
        try {
            while (!queue.isEmpty()) {
                Event top = queue.remove();
                Event.fireEvent(top.getTarget(), top);
            }
        } finally {
            inLoop = false;
        }
    }
}

