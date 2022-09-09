package com.mealfit.common.event.domain;

public abstract class Event {

    private long timeStamp;

    public Event() {
        this.timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
