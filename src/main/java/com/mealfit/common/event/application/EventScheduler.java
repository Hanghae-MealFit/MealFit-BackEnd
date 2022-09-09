package com.mealfit.common.event.application;

import com.mealfit.common.event.domain.EventQueue;
import com.mealfit.common.event.domain.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {

    private final EventQueue eventQueue;
    private final EventRepository eventRepository;

    public EventScheduler(EventQueue eventQueue, EventRepository eventRepository) {
        this.eventQueue = eventQueue;
        this.eventRepository = eventRepository;
    }

    @Async("taskScheduler")
    @Scheduled(fixedRate = 300)
    public void schedule() {
        new EventPoolingWorker(eventQueue, eventRepository).run();
    }
}
