package org.example.eventloop.core;

import java.util.ArrayDeque;
import java.util.Deque;

import org.example.eventloop.domain.entities.interfaces.ITask;

public class CallStack {
    private final Deque<ITask> callStack;

    public CallStack() {
        this.callStack = new ArrayDeque<>();
    }

    public void addTask(ITask task) {
        callStack.offer(task);
    }

    public ITask getNextTask() {
        return callStack.pop();
    }

    public boolean isEmpty() {
        return callStack.isEmpty();
    }
}
