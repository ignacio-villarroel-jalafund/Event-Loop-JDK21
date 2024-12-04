package org.example.eventloop.core.interfaces;

public interface Task  extends Event {
    void run();
    @Override
    default void execute() {
        run();
    }
}
