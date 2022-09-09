package com.mealfit.common.event.domain;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventQueue {

    private final Queue<EventEntry> queue;
    private final int queueSize;

    private EventQueue() {
        this.queue = new LinkedBlockingQueue<>(1000);
        this.queueSize = 1000;
    }

    public boolean offer(EventEntry eventEntry) {
        boolean returnValue = queue.offer(eventEntry);
        healthCheck();
        return returnValue;
    }

    public EventEntry poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No Event in Queue!");
        }
        EventEntry eventEntry = queue.poll();
        healthCheck();
        return eventEntry;
    }

    private int size() {
        return queue.size();
    }

    public boolean isFull() {
        return size() == queueSize;
    }

    public boolean isRemaining() {
        return size() > 0;
    }

    private void healthCheck() {
        log.info("\"total Queue Size\" : {}, \"currentQueue Size\" : {}", queueSize,
              size());
    }
}
