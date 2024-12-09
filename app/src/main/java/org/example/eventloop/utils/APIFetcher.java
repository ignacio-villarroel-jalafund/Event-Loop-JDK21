package org.example.eventloop.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.example.eventloop.core.EventLoop;
import org.example.eventloop.domain.entities.concretes.PromiseTask;

public class APIFetcher {
    private final HttpClient httpClient;
    private final EventLoop eventLoop;

    public APIFetcher(EventLoop eventLoop) {
        this.httpClient = HttpClient.newHttpClient();
        this.eventLoop = eventLoop;
    }

    public PromiseTask fetch(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = 
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        PromiseTask promiseTask = new PromiseTask(responseFuture)
            .onSuccess(response -> {
                System.out.println("Response: " + response.body());
            })
            .onError(ex -> {
                System.err.println("Error fetching URL: " + url);
                ex.printStackTrace();
            });

        eventLoop.addPromiseTask(promiseTask);
        return promiseTask;
    }
}
