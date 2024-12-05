package org.example.eventloop.timer;

import org.example.eventloop.core.interfaces.Task;

public class TimerTask implements Comparable<TimerTask> {
    private final Task task;
    private long executionTime;
    private final long interval;

    public TimerTask(Task task, long delay) {
        this(task, delay, 0);
    }

    public TimerTask(Task task, long delay, long interval) {
        this.task = task;
        this.executionTime = System.currentTimeMillis() + delay;
        this.interval = interval;
    }

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
