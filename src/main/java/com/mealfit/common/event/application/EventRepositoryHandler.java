package com.mealfit.common.event.application;

import com.mealfit.common.event.domain.Event;
import com.mealfit.common.event.domain.EventEntry;
import com.mealfit.common.event.domain.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventRepositoryHandler {

    private final EventRepository eventRepository;

    public EventRepositoryHandler(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @EventListener(Event.class)
    public void handle(Event event) {
        EventEntry eventEntry = eventRepository.save(event);
    }
}
