package org.example;

import org.example.eventloop.core.EventLoop;
import org.example.eventloop.scheduler.TaskScheduler;

public class App {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        TaskScheduler scheduler = new TaskScheduler(eventLoop);

        scheduler.submitTask(() -> System.out.println("Immediate task executed"));
        scheduler.scheduleTask(() -> System.out.println("Delayed task executed"), 2000);
        scheduler.scheduleRecurringTask(() -> System.out.println("Recurring task executed"), 1000);

        new Thread(eventLoop::start).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventLoop.stop();
    }
}
