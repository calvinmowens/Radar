package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.FontMetrics;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthView extends JComponent implements MouseListener, MouseMotionListener {

    private LocalDate date;
    private LocalDateTime currentTime;
    private HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();
    private LocalDate startDateForMonth;
    private ArrayList<Point2D> gesturePoints = new ArrayList<>();

    public MonthView() {
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

        int yFactor = (this.getHeight() - 50) / 6;
        int xFactor = (this.getWidth() - 20) / 7;

        // HORIZONTAL LINES
        int horizontalSpacing = 0;
        for (int i = 0; i < 6; i++) {
            g.drawLine(15, 45 + horizontalSpacing, this.getWidth(), 45 + horizontalSpacing);
            horizontalSpacing += yFactor;
        }

        // VERTICAL LINES LINES
        int verticalSpacing = 0;
        for (int i = 0; i < 7; i++) {
            g.drawLine(15 + verticalSpacing, 45, 15 + verticalSpacing, this.getHeight());
            verticalSpacing += xFactor;
        }

        LocalDate currDate = date.withDayOfMonth(1);
        boolean setStart = true;

        int startColumn = currDate.getDayOfWeek().getValue();
        if (startColumn == 7) {
            startColumn = 0;
        }

        currDate = currDate.minusDays(startColumn);
        startDateForMonth = currDate;

        // ROW TRACK
        for (int i = 0; i < 6; i++) {
            // COLUMN TRACK
            for (int j = 0; j < 7; j++) {
                // PRINT DAY OF WEEK AND DATE OF MONTH
                if (currDate.compareTo(LocalDate.now()) == 0) {
                    g.setColor(Color.RED);
                    String dowAbv = currDate.getDayOfWeek().toString().substring(0, 3) + " " + currDate.getDayOfMonth();
                    g.drawString(dowAbv, 20 + (xFactor * j), 65 + (yFactor * i));
                } else if (currDate.getMonth().compareTo(date.getMonth()) != 0) {
                    g.setColor(Color.LIGHT_GRAY); // TODO check color
                    String dowAbv = currDate.getDayOfWeek().toString().substring(0, 3) + " " + currDate.getDayOfMonth();
                    g.drawString(dowAbv, 20 + (xFactor * j), 65 + (yFactor * i));
                } else {
                    g.setColor(Color.GRAY);
                    String dowAbv = currDate.getDayOfWeek().toString().substring(0, 3) + " " + currDate.getDayOfMonth();
                    g.drawString(dowAbv, 20 + (xFactor * j), 65 + (yFactor * i));
                }


                // PRINT EVENTS
                if (events.get(currDate) != null && currDate.getMonth().compareTo(date.getMonth()) == 0) {
                    int cellHeightRem = ((this.getHeight() - 50) / 6) - 30;
                    int cellWidth = (this.getWidth() - 20) / 7;
                    int eventsCounter = 0;

                    if (cellHeightRem >= 25) {
                        ArrayList<Event> eventsOnDate = events.get(currDate);
                        for (Event event : eventsOnDate) {
                            Color eventColor = new Color(95, 102, 87, 125);
                            g.setColor(eventColor);
                            event.setRect(new Rectangle(20 + (xFactor * j), 75 + (yFactor * i) + (eventsCounter * 30), cellWidth - 10, 25));
                            g.fillRoundRect(20 + (xFactor * j), 75 + (yFactor * i) + (eventsCounter * 30), cellWidth - 10, 25, 20, 20);

                            g.setColor(Color.WHITE);

                            int fontSpace = cellWidth - 20;
                            FontMetrics metrics = g.getFontMetrics();
                            if (metrics.stringWidth(event.eventName) > fontSpace) {

                                String printString = "";
                                fontSpace = fontSpace - metrics.stringWidth("...");
                                for (char c : event.eventName.toCharArray()) {
                                    fontSpace -= metrics.charWidth(c);
                                    if (fontSpace >= 0) {
                                        printString += c;
                                    } else {
                                        break;
                                    }
                                }
                                printString += "...";
                                g.drawString(printString, 27 + (xFactor * j), 92 + (yFactor * i) + (eventsCounter * 30));
                            } else {
                                g.drawString(event.eventName, 27 + (xFactor * j), 92 + (yFactor * i) + (eventsCounter * 30));
                            }

                            eventsCounter++;
                            cellHeightRem -= 30;
                            if (cellHeightRem < 25) {
                                break;
                            }
                        }
                    }
                }


                // INCREMENT
                currDate = currDate.plusDays(1);
            }
        }

        if (gesturePoints.size() > 1) {
            for (int i = 0; i < gesturePoints.size() - 1; i++) {
                Point2D point1 = gesturePoints.get(i);
                Point2D point2 = gesturePoints.get(i + 1);

                g.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
            }
        }


        // --------------------
        // ITERATE THROUGH EVENTS
        // --------------------
//        if (events.get(date) != null) {
//            System.out.println("Drawing main.java.Event...");
//            int eventSpacing = 45;
//            ArrayList<Event> eventsOnDate = events.get(date);
//            for (Event event : eventsOnDate) {
//                String startTime = event.startTime;
//                String endTime = event.endTime;
//                double start;
//                double end;
//                start = getTime(startTime);
//                end = getTime(endTime);
//
//                System.out.println("Start Int: " + start + ", End Time: " + end);
//                int eventStartY = eventSpacing + (int)(50 * start);
//                Color eventColor = new Color(95, 102, 87, 125);
//                g.setColor(eventColor);
//                event.setRect(new Rectangle(60, eventStartY, 2000, (int)(50 * (end - start))));
//                g.fillRoundRect(60, eventStartY, 2000, (int)(50 * (end - start)), 20, 20);
////                g.fillRect(55, eventStartY, 2000, (50 * (end - start)));
//
//                g.setColor(Color.black);
//                g.drawString(event.eventName, 70, eventStartY + 20);
//                g.drawString(event.startTime + " - " + event.endTime, 70, eventStartY + 35);
//            }
//        }
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

    public void setGesturePoints(ArrayList<Point2D> gesturePoints) {
        this.gesturePoints = gesturePoints;
    }

    public void update(int DAY_MONTH_SETTING) {
        this.repaint();
    }

    public String dateToString() {
        return date.getMonth() + ", " + date.getYear();
    }

    public LocalDate calculateDate(int x, int y) {
        int gridX = (x - 10) / ((this.getWidth() - 20) / 7) + 1;
        int gridY = (y - 50) / ((this.getHeight() - 50) / 6) + 1;

        int addDays = (gridX - 1) + (7 * (gridY - 1));
        LocalDate returnDate = startDateForMonth.plusDays(addDays);
        return returnDate;
    }

    // --------------------
    // MOUSE LISTENER OVERRIDE
    // --------------------
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}

//view.setText("Day View: " + date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
//view.setText("Month View: " + date.getMonth() + ", " + date.getYear());
