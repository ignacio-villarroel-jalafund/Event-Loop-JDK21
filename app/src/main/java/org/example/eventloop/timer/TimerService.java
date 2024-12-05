package org.example.eventloop.timer;

import java.util.concurrent.PriorityBlockingQueue;

import org.example.eventloop.core.interfaces.Task;

public class TimerService {
    private final PriorityBlockingQueue<TimerTask> timerTasks = new PriorityBlockingQueue<>();

    public void scheduleTask(Task task, long delay) {
        timerTasks.offer(new TimerTask(task, delay));
    }

    public void scheduleRecurringTask(Task task, long interval) {
        timerTasks.offer(new TimerTask(task, interval, interval));
    }

    public TimerTask getNextDueTask() {
        long now = System.currentTimeMillis();
        TimerTask nextTask = timerTasks.peek();
        if (nextTask != null && nextTask.getExecutionTime() <= now) {
            return timerTasks.poll();
        }
        return null;
    }
}
