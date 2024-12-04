package org.example.eventloop.core;

import org.example.eventloop.core.interfaces.Event;
import org.example.eventloop.core.interfaces.Task;
import org.example.eventloop.scheduler.TimerService;
import org.example.eventloop.scheduler.TimerTask;

public class EventLoop {
    private final EventQueue eventQueue;
    private final TimerService timerService;
    private final ErrorHandler errorHandler;
    private volatile boolean isRunning;

    public EventLoop() {
        this.eventQueue = new EventQueue();
        this.timerService = new TimerService();
        this.errorHandler = new ErrorHandler();
    }

    public void start() {
        isRunning = true;
        while (isRunning) {
            processNextEvent();
            processTimerTasks();
        }
    }

    public void stop() {
        isRunning = false;
    }

    private void processNextEvent() {
        Event event = eventQueue.dequeue();
        if (event != null) {
            try {
                event.execute();
            } catch (Exception e) {
                errorHandler.handleError(e);
            }
        }
    }

    private void processTimerTasks() {
        TimerTask timerTask = timerService.getNextDueTask();
        while (timerTask != null) {
            try {
                timerTask.execute();
                if (timerTask.isRecurring()) {
                    timerTask.reschedule();
                    timerService.scheduleTask(timerTask::execute, timerTask.getExecutionTime() - System.currentTimeMillis());
                }
            } catch (Exception e) {
                errorHandler.handleError(e);
            }
            timerTask = timerService.getNextDueTask();
        }
    }

    public void submitTask(Task task) {
        eventQueue.enqueue(task);
    }

    public void setTimeout(Task task, long delay) {
        timerService.scheduleTask(task, delay);
    }

    public void setInterval(Task task, long interval) {
        timerService.scheduleRecurringTask(task, interval);
    }
}
