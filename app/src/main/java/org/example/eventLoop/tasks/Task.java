package org.example.eventLoop.tasks;

import org.example.eventLoop.models.TaskStatus;

public interface Task {
    void execute() throws Exception;
    void onError(Throwable e);
    void onComplete();
    boolean cancel();
    boolean isCancelled();
    TaskStatus getStatus();
}
