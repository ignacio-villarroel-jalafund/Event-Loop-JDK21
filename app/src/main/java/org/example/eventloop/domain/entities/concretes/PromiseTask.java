package org.example.eventloop.domain.entities.concretes;

import org.example.eventloop.domain.entities.interfaces.IPromiseTask;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PromiseTask implements IPromiseTask {
    private final CompletableFuture<HttpResponse<String>> future;
    private Consumer<HttpResponse<String>> successHandler;
    private Consumer<Throwable> errorHandler;

    public PromiseTask(CompletableFuture<HttpResponse<String>> future) {
        this.future = future;
        this.successHandler = response -> System.out.println("Default Response: " + response.body());
        this.errorHandler = Throwable::printStackTrace;
    }

    public PromiseTask onSuccess(Consumer<HttpResponse<String>> handler) {
        this.successHandler = handler;
        return this;
    }

    public PromiseTask onError(Consumer<Throwable> handler) {
        this.errorHandler = handler;
        return this;
    }

    @Override
    public void execute() {
        future.thenAccept(successHandler)
              .exceptionally(ex -> {
                  errorHandler.accept(ex);
                  return null;
              });
    }
}
