package org.example.eventloop.domain.entities.concretes;

import org.example.eventloop.domain.entities.interfaces.ITask;

public class Task implements ITask {

    private Runnable task;

    public Task(Runnable task) {
        this.task = task;
    }

    @Override
    public void execute() {
        task.run();
    }
}
