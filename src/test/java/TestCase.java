import org.junit.Assert;
import org.junit.Test;
import ru.axel.events.manager.EventsManager;
import ru.axel.events.manager.IEventExecutor;
import ru.axel.events.manager.exceptions.EventNotExistsException;
import ru.axel.events.manager.exceptions.IndexOutsideArrayException;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestCase {
    @Test
    public void mainTest() throws InterruptedException {
        final Exec exec = new Exec();
        final ExecError execError = new ExecError();

        final Logger logger = Logger.getLogger("Main");
        logger.setLevel(Level.ALL);

        final EventsManager eventsManager = new EventsManager(logger);

        eventsManager.addEvent("TEST_EVENT", exec);
        eventsManager.addEvent("TEST_EVENT_TWO", execError);

        eventsManager.emitAsync("TEST_EVENT", "2");

        Assert.assertThrows(EventNotExistsException.class, () -> {
            eventsManager.emit("TEST_EVENT1", "1");
        });

        Assert.assertThrows(IndexOutsideArrayException.class, () -> {
            eventsManager.emit("TEST_EVENT_TWO", "1");
        });

        Assert.assertThrows(java.lang.AssertionError.class, () -> {
            eventsManager.emit("TEST_EVENT", "1");
        });

        Thread.sleep(1000);
    }
}

class Exec implements IEventExecutor {
    private String method(String str) {
        return str;
    }

    @Override
    public void exec(Object... params) throws IndexOutsideArrayException {
        String str = getParam(0, params);

        assert Objects.equals(method(str), "bad");
    }
}

class ExecError implements IEventExecutor {
    private void method(String str) {
        System.out.println(str);
    }

    @Override
    public void exec(Object... params) throws IndexOutsideArrayException {
        String str = getParam(1, params);

        method(str);
    }
}
