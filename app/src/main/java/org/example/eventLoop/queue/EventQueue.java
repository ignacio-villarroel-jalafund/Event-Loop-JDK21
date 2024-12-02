package org.example.eventLoop.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.Event;

public class EventQueue {
    private final ConcurrentLinkedQueue<Event> queue = new ConcurrentLinkedQueue<>();

    public void enqueue(Event event) {
        queue.offer(event);
    }

    public Event dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
