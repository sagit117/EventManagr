package ru.axel.events.manager.exceptions;

public final class EventNotExistsException extends IllegalArgumentException {
    public EventNotExistsException(String msg) {
        super(msg);
    }
}
