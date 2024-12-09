package org.example.eventloop.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.eventloop.domain.entities.interfaces.ITask;

public class MicrotaskQueue {
    private ConcurrentLinkedQueue<ITask> microTaskQueue;

    public MicrotaskQueue() {
        this.microTaskQueue = new ConcurrentLinkedQueue<>();
    }

    public void addTask(ITask task) {
        microTaskQueue.offer(task);
    }

    public ITask getNextTask() {
        return microTaskQueue.poll();
    }

    public boolean isEmpty() {
        return microTaskQueue.isEmpty();
    }
}
