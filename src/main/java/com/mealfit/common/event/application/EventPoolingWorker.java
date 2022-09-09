package com.mealfit.common.event.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.common.event.domain.EventEntry;
import com.mealfit.common.event.domain.EventQueue;
import com.mealfit.common.event.domain.EventRepository;
import com.mealfit.common.event.domain.EventStatus;
import com.mealfit.common.event.infrastructure.EventsPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class EventPoolingWorker implements Runnable {

    private final EventQueue eventQueue;
    private final EventRepository eventRepository;


    public EventPoolingWorker(EventQueue eventQueue, EventRepository eventRepository) {
        this.eventQueue = eventQueue;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void run() {
        if (eventQueue.isRemaining()) {
            EventEntry event = eventQueue.poll();
            try {
                processing(event);
                activateSuccessHandler(event);
            } catch (Exception e) {
                activateFailHandler(event);
            }
        }
    }

    private void processing(EventEntry event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Class<?> clazz = Class.forName(event.getType());
            Object o = objectMapper.readValue(event.getPayload(), clazz);
            EventsPublisher.raise(o);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void activateSuccessHandler(EventEntry eventEntry) {
        updateStatus(eventEntry, EventStatus.SUCCESS);
    }

    private void activateFailHandler(EventEntry eventEntry) {
        updateStatus(eventEntry, EventStatus.FAIL);
    }

    private void updateStatus(EventEntry eventEntry, EventStatus status) {
        eventRepository.updateStatus(eventEntry.getId(), status);
    }
}
