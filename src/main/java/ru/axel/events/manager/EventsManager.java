package ru.axel.events.manager;

import ru.axel.events.manager.exceptions.EventNotExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class EventsManager {
    private final Logger logger;
    final private HashMap<String, List<IEventExecutor>> events = new HashMap<>();

    public EventsManager(Logger logger) {
        this.logger = logger;
    }

    public void addEvent(String eventName, IEventExecutor executor) {
        final var event = events.get(eventName);

        if (event != null) {
            event.add(executor);
        } else {
            final List<IEventExecutor> executors = new ArrayList<>();
            executors.add(executor);

            events.put(eventName, executors);
        }
    }

    public void emitAsync(String eventName, Object... params) {
        final var event = events.get(eventName);

        if (event != null) {
            event.forEach(method -> CompletableFuture
                .runAsync(() -> {
                    method.exec(params);
                })
                .exceptionally((throwable -> {
                    logger.severe(throwable.getMessage());
                    return null;
                }))
            );
        } else {
            throw new EventNotExistsException("Event: " + eventName + " not exists!");
        }
    }

    public void emit(String eventName, Object... params) {
        final var event = events.get(eventName);

        if (event != null) {
            event.forEach(method -> method.exec(params));
        } else {
            throw new EventNotExistsException("Event: " + eventName + " not exists!");
        }
    }
}
