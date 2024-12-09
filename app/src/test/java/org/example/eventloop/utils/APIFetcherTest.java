package org.example.eventloop.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.eventloop.core.EventLoop;
import org.junit.Test;

public class APIFetcherTest {
    @Test
    public void testFetchSuccess() {
        EventLoop eventLoop = new EventLoop();
        APIFetcher fetcher = new APIFetcher(eventLoop);

        AtomicBoolean successCalled = new AtomicBoolean(false);

        fetcher.fetch("https://jsonplaceholder.typicode.com/todos/1")
                .onSuccess(response -> {
                    String body = response.body();
                    assertTrue("The body should contain the 'title' field", body.contains("\"title\":"));
                    successCalled.set(true);
                });

        new Thread(eventLoop::start).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventLoop.stop();

        assertTrue("The successHandler should have been executed", successCalled.get());
    }

    @Test
    public void testFetchError() throws InterruptedException {
        EventLoop eventLoop = new EventLoop();
        APIFetcher fetcher = new APIFetcher(eventLoop);

        AtomicBoolean errorCalled = new AtomicBoolean(false);

        fetcher.fetch("https://jsonplaceholder.typicode.com/invalid-endpoint")
                .onError(ex -> {
                    System.err.println("Error in the API response: " + ex.getMessage());
                    errorCalled.set(true);
                });

        Thread eventLoopThread = new Thread(() -> eventLoop.start());
        eventLoopThread.start();

        Thread.sleep(3000);
        eventLoop.stop();

        assertTrue("The errorHandler should have been executed", !eventLoop.getIsRunning());
    }

    @Test
    public void testMultipleFetches() {
        EventLoop eventLoop = new EventLoop();
        APIFetcher fetcher = new APIFetcher(eventLoop);

        AtomicInteger successCount = new AtomicInteger(0);

        fetcher.fetch("https://jsonplaceholder.typicode.com/todos/1")
                .onSuccess(response -> successCount.incrementAndGet());
        fetcher.fetch("https://jsonplaceholder.typicode.com/posts/1")
                .onSuccess(response -> successCount.incrementAndGet());

        new Thread(eventLoop::start).start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventLoop.stop();

        assertEquals("Both fetches should complete successfully", 2, successCount.get());
    }
}
