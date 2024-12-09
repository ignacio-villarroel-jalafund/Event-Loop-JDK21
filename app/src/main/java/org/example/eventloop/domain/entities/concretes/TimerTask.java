package org.example.eventloop.domain.entities.concretes;

import org.example.eventloop.domain.entities.interfaces.ITask;

public class TimerTask implements ITask, Comparable<TimerTask> {
    private final Runnable task;
    private long executionTime;
    private long interval = 0;

    public TimerTask(Runnable task, long delay) {
        this.task = task;
        this.executionTime = System.currentTimeMillis() + delay;
    }

    public TimerTask(Runnable task, long delay, long interval) {
        this.task = task;
        this.executionTime = System.currentTimeMillis() + delay;
        this.interval = interval;
    }

    @Override
    public void execute() {
        task.run();
    }

    public boolean isRecurring() {
        return interval > 0;
    }

    public void reschedule() {
        this.executionTime = System.currentTimeMillis() + interval;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public int compareTo(TimerTask other) {
        return Long.compare(this.executionTime, other.executionTime);
    }
}
