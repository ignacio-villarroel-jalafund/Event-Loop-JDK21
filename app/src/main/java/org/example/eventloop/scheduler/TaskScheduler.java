package org.example.eventloop.scheduler;

import org.example.eventloop.core.EventLoop;
import org.example.eventloop.core.interfaces.Task;

public class TaskScheduler {
    private final EventLoop eventLoop;

    public TaskScheduler(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void submitTask(Task task) {
        eventLoop.submitTask(task);
    }

    public void scheduleTask(Task task, long delay) {
        eventLoop.setTimeout(task, delay);
    }

    public void scheduleRecurringTask(Task task, long interval) {
        eventLoop.setInterval(task, interval);
    }
}
