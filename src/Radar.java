import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Radar extends JFrame {

    static LocalDate date = LocalDate.now();

    static JMenuBar mb;

    // Menus
    static JMenu file;
    static JMenu viewMenu;
    // currView...
    // 0 : Day
    // 1 : Month
    static int currView = 0;

    // Menu Items
    static JMenuItem exit, day, month;

    // Content Pane Items
    static JPanel container;
    static JPanel buttonContainer;
    static JButton today;
    static JPanel nextPrevContainer;
    static JButton next;
    static JButton prev;
    static JButton newAppt;
    static JLabel view;

    static JLabel statusBarText;

    // Appointment Creation
    static JPanel newApptCP;
    static JDialog newApptDialog;

    // create a frame
    static JFrame f;
    static JPanel cp;

    public static void main(String[] args) {

        System.out.println(date);
        // create a frame
        f = new JFrame("Radar");
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
            System.out.println("Frame disposed.");
            f.dispose();
        });

        day = new JMenuItem("Day");
        day.addActionListener(e -> {
            statusBarText.setText("VIEW CHANGED: DAY");
            currView = 0;
            updateView();
        });
        month = new JMenuItem("Month");
        month.addActionListener(e -> {
            statusBarText.setText("VIEW CHANGED: MONTH");
            currView = 1;
            updateView();
        });

        // add menu items to menu
        file.add(exit);
        viewMenu.add(day);
        viewMenu.add(month);

        // add menu to menu bar
        mb.add(file);
        mb.add(viewMenu);

        statusBarText = new JLabel("STATUS TEXT HERE", SwingConstants.CENTER);
        view = new JLabel("Day View, DAY, DATE, YEAR");
        updateView();

        // create main elements
        container = new JPanel(new FlowLayout());
        buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.PAGE_AXIS));

        today = new JButton("Today");
        today.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: TODAY");
            date = LocalDate.now();
            updateView();
        });

        nextPrevContainer = new JPanel(new FlowLayout());
        prev = new JButton("<");
        prev.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: PREVIOUS");
            if (currView == 0) {
                date = date.minusDays(1);
            } else {
                date = date.minusMonths(1);
            }
            updateView();
        });
        next = new JButton(">");
        next.addActionListener(e -> {
            statusBarText.setText("BUTTON PRESSED: NEXT");
            if (currView == 0) {
                date = date.plusDays(1);
            } else {
                date = date.plusMonths(1);
            }
            updateView();
        });

        newAppt = new JButton("+");
        newAppt.addActionListener(e -> {
            // TODO appt popup

        });

        cp.add(statusBarText, BorderLayout.PAGE_END);
        nextPrevContainer.add(prev);
        nextPrevContainer.add(next);
        buttonContainer.add(today);
        buttonContainer.add(nextPrevContainer);
        buttonContainer.add(newAppt);
        container.add(buttonContainer);
        container.add(view);
        cp.add(container, BorderLayout.CENTER);

        // add menubar to frame
        f.setJMenuBar(mb);
        f.setContentPane(cp);

        // set the size of the frame
        f.setSize(500, 500);
        f.setVisible(true);
    }

    public static void updateView() {
        if (currView == 0) {
            view.setText("Day View: " + date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
        } else {
            view.setText("Month View: " + date.getMonth() + ", " + date.getYear());
        }
    }
}
