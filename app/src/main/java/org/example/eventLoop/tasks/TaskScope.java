package org.example.eventLoop.tasks;

import org.example.eventLoop.models.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class TaskScope {
    private final List<Task> subtasks = new ArrayList<>();

    public static final class ShutdownOnSuccess extends TaskScope {}
    public static final class ShutdownOnFailure extends TaskScope {}

    public void fork(Task task) {
        subtasks.add(task);
    }

    public void join() throws Exception {
        for (Task task : subtasks) {
            if (task.getStatus() == TaskStatus.FAILED) {
                throw new Exception("Task failed during execution");
            }
        }
    }
}
