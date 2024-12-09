package org.example.eventloop.domain.entities.concretes;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testTaskExecution() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Task task = new Task(() -> executed.set(true));
        task.execute();

        assertTrue("The task should have been executed", executed.get());
    }
}
