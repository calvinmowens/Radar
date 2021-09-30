package main.java.myPackage;

import java.awt.*;
import java.time.LocalDate;

public class Event {

    String eventName;
    LocalDate eventDate;
    String startTime;
    String endTime;
    Rectangle rect;

    public Event(String eventName, LocalDate eventDate, String startTime, String endTime) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString() {
        return "NEW EVENT: " + eventName + ", " + eventDate.toString() + ", " + startTime + " - " + endTime;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRect() {
        return rect;
    }
}
