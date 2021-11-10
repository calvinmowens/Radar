package main.java;

import dollar.Result;

import java.awt.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event {

    String eventName;
    LocalDate eventDate;
    LocalTime startTime;
    LocalTime endTime;
    Rectangle rect;
    Rectangle dateClickRect;
    boolean school;
    boolean friends;
    boolean church;
    boolean vacation;

    public Event(String eventName, LocalDate eventDate, String startTime, String endTime, boolean school, boolean friends, boolean church, boolean vacation) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        if (startTime.charAt(1) == ':') {
            startTime = "0" + startTime;
        }
        this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
        if (endTime.charAt(1) == ':') {
            endTime = "0" + endTime;
        }
        this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
        this.school = school;
        this.friends = friends;
        this.church = church;
        this.vacation = vacation;

//        this.startTime = LocalTime.of(Integer.parseInt(startTime.substring(0, 1)), Integer.parseInt(startTime.substring(2, 4)));
//        this.endTime = LocalTime.of(Integer.parseInt(endTime.substring(0, 1)), Integer.parseInt(endTime.substring(2, 4)));
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

    public void setDateClickedRect(Rectangle rect) {
        this.dateClickRect = rect;
    }

    public void applyGesture(Result result, int DAY_MONTH_SETTING) {
        if (DAY_MONTH_SETTING == 0) {
            System.out.println("DAY GESTURE: " + result.getName() + " applied to " + this.eventName);
        } else {
            System.out.println("MONTH GESTURE: " + result.getName() + " applied to " + this.eventName);
        }
    }
}
