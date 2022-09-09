package com.mealfit.common.event.domain;

import java.util.List;

public interface EventRepository {

    EventEntry save(Object event);

    void updateStatus(long id, EventStatus status);

    List<EventEntry> get(long offset, long limit);
}
