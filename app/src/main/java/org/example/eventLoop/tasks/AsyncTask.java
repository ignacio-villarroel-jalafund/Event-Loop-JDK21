package org.example.eventLoop.tasks;

import org.example.eventLoop.models.TaskPriority;
import org.example.eventLoop.models.TaskStatus;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class AsyncTask<T> extends BaseTask {
    private final ExecutorService executor;
    private final Callable<T> operation;
    private final Duration timeout;
    private volatile T result;
    private volatile Future<T> future;
    private final List<Consumer<T>> successCallbacks;
    private final List<Consumer<Throwable>> errorCallbacks;

    public AsyncTask(String Id, Callable<T> operation, TaskPriority priority, Duration timeout) {
        super(Id, priority);
        this.operation = operation;
        this.timeout = timeout;
        this.executor = Executors.newSingleThreadExecutor(
                Thread.ofVirtual().name("async-task-" + getId()).factory()
        );
        this.successCallbacks = new CopyOnWriteArrayList<>();
        this.errorCallbacks = new CopyOnWriteArrayList<>();
    }

    @Override
    public void execute() throws Exception {
        if (isCancelled()) {
            return;
        }

        setStatus(TaskStatus.RUNNING);
        try {
            future = executor.submit(operation);
            result = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            setStatus(TaskStatus.COMPLETED);
            notifySuccess(result);
        } catch (Exception e) {
            setStatus(TaskStatus.FAILED);
            notifyError(e);
            throw e;
        } finally {
            executor.shutdown();
        }
    }

    public AsyncTask<T> onSuccess(Consumer<T> callback) {
        successCallbacks.add(callback);
        if (status == TaskStatus.COMPLETED && result != null) {
            callback.accept(result);
        }
        return this;
    }

    public AsyncTask<T> onError(Consumer<Throwable> callback) {
        errorCallbacks.add(callback);
        return this;
    }

    private void notifySuccess(T value) {
        successCallbacks.forEach(callback -> callback.accept(value));
    }

    private void notifyError(Throwable error) {
        errorCallbacks.forEach(callback -> callback.accept(error));
    }

    public T getResult() {
        if (status != TaskStatus.COMPLETED) {
            throw new IllegalStateException("Task has failed");
        }
        return result;
    }

    @Override
    public boolean cancel() {
        if (super.cancel()) {
            if (future != null) {
                future.cancel(true);
            }
            executor.shutdownNow();
            return true;
        }
        return false;
    }
}
