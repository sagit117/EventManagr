package ru.axel.events.manager;

import org.jetbrains.annotations.NotNull;
import ru.axel.events.manager.exceptions.IndexOutsideArrayException;

import java.util.Arrays;

public interface IEventExecutor {
    @SuppressWarnings("unchecked")
    default <R> R getParam(int index, Object @NotNull ...params) throws IndexOutsideArrayException {
        if (index >= Arrays.stream(params).count()) {
            throw new IndexOutsideArrayException("index[" + index + "] outside array!");
        }

        return (R) params[index];
    }

    void exec(Object... params) throws IndexOutsideArrayException;
}
