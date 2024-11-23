package org.example.eventLoop.tasks;

import org.example.eventLoop.models.TaskPriority;
import org.example.eventLoop.models.TaskStatus;

import java.time.Duration;
import java.time.Instant;

public class TimerTask extends BaseTask {
    private final Task task;
    private final Instant executeAt;
    private final Duration interval;
    private final boolean recurring;

    public TimerTask(String id, Task task, Instant executeAt, Duration interval, boolean recurring) {
        super(id, task instanceof BaseTask ? ((BaseTask) task).getPriority() : TaskPriority.MEDIUM);
        this.task = task;
        this.executeAt = executeAt;
        this.interval = interval;
        this.recurring = recurring;
    }

    @Override
    public void execute() throws Exception {
        if (!isCancelled()) {
            setStatus(TaskStatus.RUNNING);
            try {
                task.execute();
                onComplete();
            } catch (Exception e) {
                onError(e);
                throw e;
            }
        }
    }

    public boolean isReady() {
        return !isCancelled() && Instant.now().isAfter(executeAt);
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void reschedule() {
        if (recurring && !isCancelled()) {
            setStatus(TaskStatus.PENDING);
        }
    }

    @Override
    public boolean cancel() {
        if (super.cancel()) {
            return task.cancel();
        }
        return false;
    }
}
