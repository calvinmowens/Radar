package main.java.myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DayView extends JComponent implements MouseListener, MouseMotionListener {

    private LocalDate date;
    private LocalDateTime currentTime;
    private HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

    public DayView() {
        date = LocalDate.now();
        addMouseListener(this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DayView view = new DayView();  // Initialize the component.
        frame.getContentPane().add(view);      // Place the component on the application
        // window such that it fills the whole
        // window frame.
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        currentTime = LocalDateTime.now();

        g.setColor(Color.lightGray);
        g.setFont(new Font("Calibri", Font.PLAIN, 18));
        g.drawString(dateToString(), 10, 20);
        String timeString = currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond();
        g.drawString(timeString, 350, 20);
        g.setFont(new Font("Calibri", Font.PLAIN, 15));

        g.drawString("0:00", 10, 50);
        g.drawLine(50, 45, 2000, 45);

        int time = 0;
        int spacing = 50;
        for (int i = 0; i < 24; i++) {
            g.drawString(time + ":00", 10, spacing);
            g.drawLine(55, spacing-5, 2000, spacing-5);
            time++;
            spacing += 50;
        }

        if (events.get(date) != null) {
            System.out.println("Drawing main.java.myPackage.Event...");
            int eventSpacing = 45;
            ArrayList<Event> eventsOnDate = events.get(date);
            for (Event event : eventsOnDate) {
                String startTime = event.startTime;
                String endTime = event.endTime;
                double start;
                double end;
                start = getTime(startTime);
                end = getTime(endTime);

                System.out.println("Start Int: " + start + ", End Time: " + end);
                int eventStartY = eventSpacing + (int)(50 * start);
                Color eventColor = new Color(95, 102, 87, 125);
                g.setColor(eventColor);
                event.setRect(new Rectangle(60, eventStartY, 2000, (int)(50 * (end - start))));
                g.fillRoundRect(60, eventStartY, 2000, (int)(50 * (end - start)), 20, 20);
//                g.fillRect(55, eventStartY, 2000, (50 * (end - start)));

                g.setColor(Color.black);
                g.drawString(event.eventName, 70, eventStartY + 20);
                // TODO add conditional around rect to display time
                // TODO change font size here
                g.drawString(event.startTime + " - " + event.endTime, 70, eventStartY + 35);
            }
        }
    }

    private double getTime(String time) {
        int hour;
        double minute;
        double result;
        if (time.length() == 4) {
            hour = Integer.parseInt(String.valueOf(time.charAt(0)));
            minute = (Integer.parseInt(time.substring(2))) * 1.0 / 60;
        } else {
            hour = Integer.parseInt(time.substring(0, 2));
            minute = (Integer.parseInt(time.substring(3))) * 1.0 / 60;
        }
        result = hour + minute;
        return result;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        date = newDate;
    }

    public HashMap<LocalDate, ArrayList<Event>> getMap() {
        return events;
    }

    public void setMap(HashMap<LocalDate, ArrayList<Event>> events) {
        this.events = events;
    }

    public void update(int DAY_MONTH_SETTING) {
        System.out.println("UPDATE: Repainting DayView");
        this.repaint();
    }

    public String dateToString() {
        return date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear();
    }

    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("mouse entry");
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse Dragged.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

//view.setText("Day View: " + date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
//view.setText("Month View: " + date.getMonth() + ", " + date.getYear());
