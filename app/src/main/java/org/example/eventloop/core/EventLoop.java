package org.example.eventloop.core;

import org.example.eventloop.domain.entities.concretes.PromiseTask;
import org.example.eventloop.domain.entities.concretes.Task;
import org.example.eventloop.domain.entities.concretes.TimerTask;
import org.example.eventloop.domain.entities.interfaces.ITask;

public class EventLoop {
    private CallStack callStack;
    private TaskQueue taskQueue;
    private MicrotaskQueue microtaskQueue;
    private ErrorHandler errorHandler;
    private boolean isRunning;

    public EventLoop() {
        this.callStack = new CallStack();
        this.taskQueue = new TaskQueue();
        this.microtaskQueue = new MicrotaskQueue();
        this.errorHandler = new ErrorHandler();
    }

    public void start() {
        isRunning = true;
        while (isRunning) {
            if (!callStack.isEmpty()) {
                try {
                    ITask task = callStack.getNextTask();
                    task.execute();
                } catch (Exception e) {
                    errorHandler.handleError(e);
                }
            } else if (!microtaskQueue.isEmpty()) {
                ITask microtask = microtaskQueue.getNextTask();
                callStack.addTask(microtask);
            } else if (!taskQueue.isEmpty()) {
                TimerTask task = (TimerTask) taskQueue.getNextTask();
                if (task.getExecutionTime() <= System.currentTimeMillis()) {
                    callStack.addTask(task);
                    if (task.isRecurring()) {
                        task.reschedule();
                        taskQueue.addTask(task);
                    }
                } else {
                    taskQueue.addTask(task);
                }
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void addTask(Task task) {
        callStack.addTask(task);
    }

    public void scheduleTask(TimerTask task) {
        taskQueue.addTask(task);
    }

    public void addPromiseTask(PromiseTask task) {
        microtaskQueue.addTask(task);
    }
}
