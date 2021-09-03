import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Radar extends JFrame {

    static JMenuBar mb;

    // Menus
    static JMenu file;
    static JMenu view;

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
    static JLabel currentView;

    static JLabel status;

    // create a frame
    static JFrame f;
    static JPanel cp;

    public static void main(String[] args) {

        // create a frame
        f = new JFrame("Radar");
        cp = new JPanel(new BorderLayout());

        // create a menubar
        mb = new JMenuBar();

        // create a menu
        file = new JMenu("File");
        view = new JMenu("View");

        // create menu items
        exit = new JMenuItem("Exit");
        exit.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Frame disposed.");
                f.dispose();
            }
        });

        day = new JMenuItem("Day");
        month = new JMenuItem("Month");

        // add menu items to menu
        file.add(exit);
        view.add(day);
        view.add(month);

        // add menu to menu bar
        mb.add(file);
        mb.add(view);

        // create main elements
        container = new JPanel(new FlowLayout());
        buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.PAGE_AXIS));
        today = new JButton("Today");
        nextPrevContainer = new JPanel(new FlowLayout());
        prev = new JButton("<");
        next = new JButton(">");
        newAppt = new JButton("+");

        status = new JLabel("STATUS TEXT HERE");
        currentView = new JLabel("Day View, DAY, DATE, YEAR");

        cp.add(status, BorderLayout.PAGE_END);
        nextPrevContainer.add(prev);
        nextPrevContainer.add(next);
        buttonContainer.add(today);
        buttonContainer.add(nextPrevContainer);
        buttonContainer.add(newAppt);
        container.add(buttonContainer);
        container.add(currentView);
        cp.add(container, BorderLayout.CENTER);

        // add menubar to frame
        f.setJMenuBar(mb);
        f.setContentPane(cp);

        // set the size of the frame
        f.setSize(500, 500);
        f.setVisible(true);
    }

}
