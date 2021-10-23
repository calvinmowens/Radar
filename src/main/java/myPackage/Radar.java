package main.java.myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

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

    // main.java.myPackage.DayView Object Declaration, initializes to LocalDate.now()
    static JScrollPane scroll;
    static DayView dayView = new DayView();
    static MonthView monthView = new MonthView();
    static int timeHour = LocalDateTime.now().getHour();

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

        System.out.println(dayView.getDate());

        // create a menubar
        mb = new JMenuBar();

        // create a menu
        file = new JMenu("File");
        viewMenu = new JMenu("View");

        // create menu items
        exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            statusBarText.setText("EXIT, BYE BYE");
            System.out.println("Frame disposed.");
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
                System.out.println("Window Resized.");
                System.out.println("Month Dimensions: " + monthView.getWidth() + ", " + monthView.getHeight());
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
                System.out.println("Mouse Clicked.");
                if (e.getClickCount() % 2 == 0) {

                    System.out.println("Mouse Double-Clicked.");

                    if (events.get(dayView.getDate()) != null) {
                        int x = e.getX();
                        int y = e.getY();
                        ArrayList<Event> eventsOnDate = events.get(dayView.getDate());
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(x, y)) {
                                eventsOnDate.remove(event);
                                editEvent(event);
                                return;
                            }
                        }
                        System.out.println("You shouldn't be here on event clicked.");
                        createEventTime(y);
                    } else {
                        int y = e.getY();
                        timeHour = 1;
                        createEventTime(y);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("Mouse Dragged");
            }
        });

//        monthView.setPreferredSize( new Dimension(500, 1250));
//        monthView.setMaximumSize(monthView.getPreferredSize());
        monthView.setPreferredSize( new Dimension(500, (int)cp.getBounds().getSize().getHeight()) );
        monthView.setMinimumSize(monthView.getPreferredSize());
        monthView.setMap(events);
        monthView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse Clicked.");
                if (e.getClickCount() % 2 == 0) {

                    System.out.println("Mouse Double-Clicked.");

                    if (events.get(dayView.getDate()) != null) {
                        int x = e.getX();
                        int y = e.getY();
                        ArrayList<Event> eventsOnDate = events.get(dayView.getDate());
                        for (Event event : eventsOnDate) {
                            if (event.rect.contains(x, y)) {
                                eventsOnDate.remove(event);
                                editEvent(event);
                                return;
                            }
                        }
                        System.out.println("You shouldn't be here on event clicked.");
                        createEventTime(y);
                    } else {
                        int y = e.getY();
                        timeHour = 1;
                        createEventTime(y);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("Mouse Dragged");
            }
        });


        scroll = new JScrollPane();
        scroll.setPreferredSize( new Dimension(500, (int)f.getBounds().getSize().getHeight()) );
        scroll.setMinimumSize(scroll.getPreferredSize());
        scroll.setViewportView(DAY_MONTH_SETTING == 0 ? dayView : monthView);

        System.out.println((int)f.getBounds().getSize().getHeight());

        dayView.update(DAY_MONTH_SETTING);
        monthView.update(DAY_MONTH_SETTING);

        today = new JButton("Today");
        today.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: TODAY");
            dayView.setDate(LocalDate.now());
            dayView.update(DAY_MONTH_SETTING);
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
            startTime = new JTextField(x.startTime);
            endTime = new JTextField(x.endTime);
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
            dayView.update(DAY_MONTH_SETTING);
            System.out.println(events.get(dayView.getDate()));
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
        System.out.println(y);

        Event newEvent = new Event("New Event", dayView.getDate(), y+":00", (y+1)+":00");
        editEvent(newEvent);
    }
}
