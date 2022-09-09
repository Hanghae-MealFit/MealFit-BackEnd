package com.mealfit.common.event.domain;

import lombok.Getter;

@Getter
public class EventEntry {

    private Long id;
    private String type;
    private String contentType;
    private String payload;
    private long timeStamp;
    private EventStatus status;

    public EventEntry(String type, String contentType, String payload) {
        this.type = type;
        this.contentType = contentType;
        this.payload = payload;
        this.timeStamp = System.currentTimeMillis();
        this.status = EventStatus.STAND_BY;
    }

    public EventEntry(Long id, String type, String contentType, String payload, String status, long timeStamp) {
        this.id = id;
        this.type = type;
        this.contentType = contentType;
        this.payload = payload;
        this.status = EventStatus.valueOf(status);
        this.timeStamp = timeStamp;
    }
}
