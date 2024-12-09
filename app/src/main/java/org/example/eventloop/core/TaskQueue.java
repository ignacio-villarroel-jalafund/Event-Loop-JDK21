package org.example.eventloop.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.eventloop.domain.entities.interfaces.ITask;

public class TaskQueue {
    private ConcurrentLinkedQueue<ITask> taskQueue;

    public TaskQueue() {
        this.taskQueue = new ConcurrentLinkedQueue<>();
    }

    public void addTask(ITask task) {
        taskQueue.offer(task);
    }

    public ITask getNextTask() {
        return taskQueue.poll();
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }
}
