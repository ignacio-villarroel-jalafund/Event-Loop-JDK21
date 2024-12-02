package org.example.eventLoop.interfaces;

public interface Task  extends Event {
    void run();
    @Override
    default void execute() {
        run();
    }
}
