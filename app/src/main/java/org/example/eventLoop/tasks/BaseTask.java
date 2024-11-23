package org.example.eventLoop.tasks;

import org.example.eventLoop.models.TaskPriority;
import org.example.eventLoop.models.TaskStatus;


public abstract class BaseTask implements Task {
    protected final String id;
    protected TaskStatus status;
    protected TaskPriority priority;
    private volatile boolean cancelled;

    protected BaseTask(String id, TaskPriority priority) {
        this.id = id;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.cancelled = false;
    }

    @Override
    public abstract void execute() throws Exception;

    @Override
    public void onError(Throwable e) {
        setStatus(TaskStatus.FAILED);
    }

    @Override
    public void onComplete() {
        setStatus(TaskStatus.COMPLETED);
    }

    @Override
    public boolean cancel() {
        if (status == TaskStatus.RUNNING || status == TaskStatus.COMPLETED) {
            return false;
        }
        cancelled = true;
        setStatus(TaskStatus.CANCELLED);
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }
}
