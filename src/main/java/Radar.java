package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import dollar.*;

public class Radar extends JFrame {

    /*
    Frame
    |   Content Pane (cp)
    |   |   Container
    |   |   |   Scroll Pane
    |   |   |   |   DayView OR
    |   |   |   |   MonthView
    |   |   |   Button Container
    |   |   Status Bar
    |   |   File
     */

    // main.java.DayView Object Declaration, initializes to LocalDate.now()
    private DayView dayView2 = new DayView();
    static JScrollPane scroll;
    static DayView dayView = new DayView();
    static MonthView monthView = new MonthView();
    static int timeHour = LocalDateTime.now().getHour();

    static DollarRecognizer dollar = new DollarRecognizer();
    public static ArrayList<Point2D> gesturePoints = new ArrayList<>();
    static Event targetEvent;
    static LocalDate monthGestureTargetDate;

    static JMenuBar mb;

    // Menus
    static JMenu file;
    static JMenu viewMenu;

    // currView...
    // 0 : Day
    // 1 : Month
    static int DAY_MONTH_SETTING = 0;

    // Menu Items
    static JMenuItem exit, day, month;

    // Content Pane Items
    static Box container;
    static JPanel buttonContainer;
    static JButton today;
    static JPanel nextPrevContainer;
    static JButton next;
    static JButton prev;
    static JButton newEvent;

    // status bar at bottom
    static JLabel statusBarText;

    // create a frame
    static JFrame f;
    static JPanel cp;

    public static HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

    public static void main(String[] args) {

        // create a frame
        f = new JFrame("Radar");
        f.setSize(800, 800);
        cp = new JPanel(new BorderLayout());

        // create a menubar
        mb = new JMenuBar();

        // create a menu
        file = new JMenu("File");
        viewMenu = new JMenu("View");

        // create menu items
        exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            statusBarText.setText("EXIT, BYE BYE");
            f.dispose();
        });

        day = new JMenuItem("Day");
        day.addActionListener(e -> {
            statusBarText.setText("VIEW CHANGED: DAY");
            DAY_MONTH_SETTING = 0;
            dayView.update(DAY_MONTH_SETTING);
            scroll.setViewportView(dayView);
        });
        month = new JMenuItem("Month");
        month.addActionListener(e -> {
            statusBarText.setText("VIEW CHANGED: MONTH");
            DAY_MONTH_SETTING = 1;
            monthView.update(DAY_MONTH_SETTING);
            scroll.setViewportView(monthView);
        });

        // add menu items to menu
        file.add(exit);
        viewMenu.add(day);
        viewMenu.add(month);

        // add menu to menu bar
        mb.add(file);
        mb.add(viewMenu);

        statusBarText = new JLabel("STATUS TEXT HERE", SwingConstants.CENTER);

        // --------------------
        // CREATE CONTAINERS AND VIEW
        // --------------------

        class ResizeListener extends ComponentAdapter {
            public void componentResized(ComponentEvent e) {
                monthView.setPreferredSize( new Dimension(500, (int)cp.getBounds().getSize().getHeight()) );
                monthView.setMinimumSize(monthView.getPreferredSize());
                statusBarText.setText("Window Resized (cp) - w:" + cp.getWidth() + ", h:" + cp.getHeight());
            }
        }

        container = Box.createHorizontalBox();

        buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(0,1,0,3));
        buttonContainer.setPreferredSize( new Dimension(200, 100));
        buttonContainer.setMaximumSize(buttonContainer.getPreferredSize());

        dayView.setPreferredSize( new Dimension(500, 1250));
        dayView.setMaximumSize(dayView.getPreferredSize());
        dayView.setMap(events);
        dayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0) {

                    int x = e.getX();
                    int y = e.getY();

                    if (events.get(dayView.getDate()) != null) {
                        ArrayList<Event> eventsOnDate = events.get(dayView.getDate());
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(x, y)) {
                                statusBarText.setText("EDIT EVENT: " + event.eventName);
                                eventsOnDate.remove(event);
                                editEvent(event);
                                return;
                            }
                        }
                        System.out.println("DEBUG: You shouldn't be here on event clicked.");
                        createEventTime(y);
                    } else {
                        timeHour = 1;
                        createEventTime(y);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (events.get(dayView.getDate()) != null) {
                        ArrayList<Event> eventsOnDate = events.get(dayView.getDate());
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(e.getX(), e.getY())) {
                                targetEvent = event;
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (gesturePoints.size() != 0) {
                        Result gesture = dollar.recognize(gesturePoints);
                        if (targetEvent != null) {
                            handleGesture(gesture);
                            statusBarText.setText("GESTURE: " + gesture.getName() + " -> " + targetEvent.eventName);
                        } else {
                            handleGesture(gesture);
                            statusBarText.setText("GESTURE: " + gesture.getName() + " -> " + "NO TARGET EVENT");
                        }
                    }
                    gesturePoints.clear();
                    targetEvent = null;
                    dayView.update(DAY_MONTH_SETTING);
                }
            }
        });
        dayView.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    gesturePoints.add(new Point2D.Double(e.getX(), e.getY()));
                    dayView.setGesturePoints(gesturePoints);
                    dayView.update(DAY_MONTH_SETTING);
                }
            }
        });

//        monthView.setPreferredSize( new Dimension(500, 1250));
//        monthView.setMaximumSize(monthView.getPreferredSize());
        monthView.setPreferredSize( new Dimension((int)cp.getBounds().getSize().getWidth(), (int)cp.getBounds().getSize().getHeight()));
        monthView.setMinimumSize(monthView.getPreferredSize());
        monthView.setMap(events);
        monthView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0) {
                    int x = e.getX();
                    int y = e.getY();

                    LocalDate clickedDate = monthView.calculateDate(x, y);
                    if (events.get(clickedDate) != null) {
                        ArrayList<Event> eventsOnDate = events.get(clickedDate);
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(x, y)) {
                                eventsOnDate.remove(event);
                                editEvent(event);
                                return;
                            }
                        }
                    }
                    statusBarText.setText("CREATE EVENT IN MONTH VIEW");
                    createEventDate(clickedDate);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    LocalDate clickedDate = monthView.calculateDate(e.getX(), e.getY());
                    monthGestureTargetDate = clickedDate;
                    if (events.get(clickedDate) != null) {
                        ArrayList<Event> eventsOnDate = events.get(clickedDate);
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(e.getX(), e.getY())) {
                                targetEvent = event;
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Result gesture = dollar.recognize(gesturePoints);
                    if (targetEvent != null) {
                        handleGesture(gesture);
                        statusBarText.setText("GESTURE: " + gesture.getName() + " -> " + targetEvent.eventName);
                    } else {

                        statusBarText.setText("GESTURE: " + gesture.getName() + " -> " + "NO TARGET EVENT");
                    }
                    gesturePoints.clear();
                    targetEvent = null;
                    monthView.update(DAY_MONTH_SETTING);
                }
            }
        });

        monthView.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    statusBarText.setText("GESTURE STARTED");
                    gesturePoints.add(new Point2D.Double(e.getX(), e.getY()));
                    monthView.setGesturePoints(gesturePoints);
                    monthView.update(DAY_MONTH_SETTING);
                }
            }
        });


        scroll = new JScrollPane();
        scroll.setPreferredSize( new Dimension(500, (int)f.getBounds().getSize().getHeight()) );
        scroll.setMinimumSize(scroll.getPreferredSize());
        scroll.setViewportView(DAY_MONTH_SETTING == 0 ? dayView : monthView);

        dayView.update(DAY_MONTH_SETTING);
        monthView.update(DAY_MONTH_SETTING);

        today = new JButton("Today");
        today.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: TODAY");
            dayView.setDate(LocalDate.now());
            monthView.setDate(LocalDate.now());
            dayView.update(DAY_MONTH_SETTING);
            monthView.update(DAY_MONTH_SETTING);
        });

        nextPrevContainer = new JPanel(new FlowLayout());
        prev = new JButton("<");
        prev.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: PREVIOUS");
            if (DAY_MONTH_SETTING == 0) {
                dayView.setDate(dayView.getDate().minusDays(1));
                monthView.setDate(monthView.getDate().minusDays(1));
            } else {
                dayView.setDate(dayView.getDate().minusMonths(1));
                monthView.setDate(monthView.getDate().minusMonths(1));
            }
            dayView.update(DAY_MONTH_SETTING);
            monthView.update(DAY_MONTH_SETTING);
        });
        next = new JButton(">");
        next.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: NEXT");
            if (DAY_MONTH_SETTING == 0) {
                dayView.setDate(dayView.getDate().plusDays(1));
                monthView.setDate(monthView.getDate().plusDays(1));
            } else {
                dayView.setDate(dayView.getDate().plusMonths(1));
                monthView.setDate(monthView.getDate().plusMonths(1));
            }
            dayView.update(DAY_MONTH_SETTING);
            monthView.update(DAY_MONTH_SETTING);
        });

        newEvent = new JButton("+");
        newEvent.addActionListener(Radar::createEvent);

        f.addComponentListener(new ResizeListener());

        // --------------------
        // ADD STUFF TO FRAME
        // --------------------

        nextPrevContainer.add(prev);
        nextPrevContainer.add(next);

        buttonContainer.add(today);
        buttonContainer.add(nextPrevContainer);
        buttonContainer.add(newEvent);

        container.add(buttonContainer);
        container.add(scroll);

        cp.add(container, BorderLayout.CENTER);
        cp.add(statusBarText, BorderLayout.PAGE_END);

        // add menubar to frame
        f.setJMenuBar(mb);
        f.setContentPane(cp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationByPlatform( true );
        f.pack();
        f.setVisible( true );
    }

    public static void editEvent(Event x) {
        JTextField eventName;
        JTextField eventDate;
        JTextField startTime;
        JTextField endTime;
        JCheckBox option1;
        JCheckBox option2;
        JCheckBox option3;
        JCheckBox option4;

        if (x == null) {
            // Field Creation
            eventName = new JTextField("New Event");
            eventDate = new JTextField(dayView.getDate().toString());
//            JPanel timeContainer = new JPanel(new FlowLayout());
            startTime = new JTextField(timeHour + ":00");
            endTime = new JTextField(timeHour + 1 + ":00");
            option1 = new JCheckBox("School");
            option2 = new JCheckBox("Family + Friends");
            option3 = new JCheckBox("Church");
            option4 = new JCheckBox("Vacation");
        } else {
            // Field Creation
            eventName = new JTextField(x.eventName);
            eventDate = new JTextField(x.eventDate.toString());
//            JPanel timeContainer = new JPanel(new FlowLayout());
            startTime = new JTextField(x.startTime.toString());
            endTime = new JTextField(x.endTime.toString());
            option1 = new JCheckBox("School");
            option2 = new JCheckBox("Family + Friends");
            option3 = new JCheckBox("Church");
            option4 = new JCheckBox("Vacation");
        }
        final JComponent[] inputs = new JComponent[] {
                new JLabel("Event Name:"),
                eventName,
                new JLabel("Event Date:"),
                eventDate,
                new JLabel("Start Time:"),
                startTime,
                new JLabel("End Time:"),
                endTime,
                option1,
                option2,
                option3,
                option4
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "NEW APPOINTMENT", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && startTime.getText().compareTo(endTime.getText()) != 0) {
            Event myEvent = new Event(eventName.getText(), LocalDate.parse(eventDate.getText()), startTime.getText(), endTime.getText());
            events.computeIfAbsent(myEvent.eventDate, k -> new ArrayList<Event>());
            ArrayList<Event> innerList = events.get(myEvent.eventDate);
            innerList.add(myEvent);

            events.put(myEvent.eventDate,innerList);
            dayView.setMap(events);
            monthView.setMap(events);
            dayView.update(DAY_MONTH_SETTING);
            monthView.update(DAY_MONTH_SETTING);
            statusBarText.setText(myEvent.toString());
        } else {
            statusBarText.setText("NEW EVENT CREATION CANCELED");
            timeHour = LocalDateTime.now().getHour();
        }
    }

    public static void createEvent(AWTEvent e) {
        editEvent(null);
    }

    public static void createEventTime(int y) {
        y = (y - 50) / 50;

        Event newEvent = new Event("New Event", dayView.getDate(), y+":00", (y+1)+":00");
        editEvent(newEvent);
    }

    public static void createEventDate(LocalDate date) {
        // TODO add code here after doing calculations
        Event newEvent = new Event("New Event", date, "12:00", "13:00");
        editEvent(newEvent);
    }

    public static void handleGesture(Result gesture) {
        if (DAY_MONTH_SETTING == 0) {
            handleGesture(gesture, dayView.getDate());
        } else {
            handleGesture(gesture, monthGestureTargetDate);
        }
    }

    public static void handleGesture(Result gesture, LocalDate targetDate) {
        ArrayList<Event> eventsOnDate = events.get(targetDate);
        switch (gesture.getName()) {
            case "delete":
                if (targetEvent != null) {
                    eventsOnDate.remove(targetEvent);
                    // status
                    break;
                }
                // status
                break;
            case "circle":
                ArrayList<Event> toRemove = new ArrayList<>();
                for (Event event : eventsOnDate) {
                    toRemove.add(event);
                }
                eventsOnDate.removeAll(toRemove);
                // status
                break;
            case "left square bracket":
                dayView.setDate(dayView.getDate().plusDays(1));
                monthView.setDate(monthView.getDate().plusDays(1));
                dayView.update(DAY_MONTH_SETTING);
                // status
                break;
            case "right square bracket":
                dayView.setDate(dayView.getDate().minusDays(1));
                monthView.setDate(monthView.getDate().minusDays(1));
                dayView.update(DAY_MONTH_SETTING);
                // status
                break;
            case "caret":
                if (targetEvent != null) {
                    targetEvent.startTime = targetEvent.startTime.minusHours(1);
                    targetEvent.endTime = targetEvent.endTime.minusHours(1); // this is dumb
                    dayView.update(DAY_MONTH_SETTING);
                    // status
                }
                break;
            case "v":
                if (targetEvent != null) {
                    targetEvent.startTime = targetEvent.startTime.plusHours(1);
                    targetEvent.endTime = targetEvent.endTime.plusHours(1);
                    dayView.update(DAY_MONTH_SETTING);
                    // status
                }
                break;
            case "star":
                break;
            case "check":
                break;
            case "x":
                break;
            case "pigtail":
                break;
            default:
                statusBarText.setText("INVALID GESTURE FOR EVENT VIEW");
        }
    }

    public static void leftBracketDrawn() {
        if (DAY_MONTH_SETTING == 0) {
            dayView.getDate().plusDays(1);
            // update
        } else {

        }
    }

    public DayView getDayView() {
        return dayView2;
    }
}
