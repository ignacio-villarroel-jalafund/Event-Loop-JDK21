package org.example.eventloop.domain.entities.concretes;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTaskTest {

    @Test
    public void testSingleExecution() {
        AtomicBoolean executed = new AtomicBoolean(false);
        long delay = 500;

        TimerTask task = new TimerTask(() -> executed.set(true), delay);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < task.getExecutionTime()) {
        }

        task.execute();

        assertTrue("The task should have been executed", executed.get());
        assertTrue("The execution time should have been completed",
                System.currentTimeMillis() >= startTime + delay);
    }

    @Test
    public void testRecurringExecution() {
        AtomicInteger executionCount = new AtomicInteger(0);
        long delay = 500;
        long interval = 300;

        TimerTask task = new TimerTask(() -> executionCount.incrementAndGet(), delay, interval);

        while (System.currentTimeMillis() < task.getExecutionTime()) {
        }
        task.execute();
        task.reschedule();

        assertEquals("The task should have been executed once", 1, executionCount.get());
        assertTrue("The task should have been rescheduled for the future.",
                task.getExecutionTime() > System.currentTimeMillis());

        while (System.currentTimeMillis() < task.getExecutionTime()) {
        }
        task.execute();
        task.reschedule();

        assertEquals("The task should have been executed twice", 2, executionCount.get());
    }

    @Test
    public void testIsRecurring() {
        TimerTask nonRecurringTask = new TimerTask(() -> {
        }, 500);
        TimerTask recurringTask = new TimerTask(() -> {
        }, 500, 1000);

        assertFalse("The task should not be recurring", nonRecurringTask.isRecurring());
        assertTrue("The task should be recurrent", recurringTask.isRecurring());
    }
}
