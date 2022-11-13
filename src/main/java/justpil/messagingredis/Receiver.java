package justpil.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private final AtomicInteger COUNTER = new AtomicInteger();

    public void receive(String message) {
        LOGGER.info("Received <" + message + ">");
        COUNTER.incrementAndGet();
    }

    public int getCount() {
        return COUNTER.get();
    }
}
