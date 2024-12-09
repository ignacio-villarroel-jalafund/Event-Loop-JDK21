package org.example;

import org.example.eventloop.core.EventLoop;
import org.example.eventloop.domain.entities.concretes.Task;
import org.example.eventloop.domain.entities.concretes.TimerTask;
import org.example.eventloop.utils.APIFetcher;

public class App {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        APIFetcher fetcher = new APIFetcher(eventLoop);

        eventLoop.addTask(new Task(() -> System.out.println("Immediate task executed")));

        eventLoop.scheduleTask(new TimerTask(() -> System.out.println("Delayed task executed"), 2000));

        eventLoop.scheduleTask(new TimerTask(() -> System.out.println("Recurring task executed"), 1000, 1000));

        fetcher.fetch("https://jsonplaceholder.typicode.com/todos/1");

        fetcher.fetch("https://jsonplaceholder.typicode.com/posts/1");

        eventLoop.addTask(new Task(() -> {
            System.out.println("Composite task showing multiple API interactions");
        }));

        new Thread(eventLoop::start).start();
        
        eventLoop.addTask(new Task(() -> {
            throw new RuntimeException("Simulated exception from task");
        }));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventLoop.stop();
    }
}
