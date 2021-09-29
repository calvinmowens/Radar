import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DayView extends JComponent {

    private LocalDate date;
    private HashMap<LocalDate, ArrayList<Event>> events = new HashMap<LocalDate, ArrayList<Event>>();

    public DayView() {
        date = LocalDate.now();

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.lightGray);
        g.setFont(new Font("Calibri", Font.PLAIN, 18));
        g.drawString(date.toString(), 10, 20);
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
            System.out.println("Drawing Event...");
            int eventSpacing = 45;
            ArrayList<Event> eventsOnDate = events.get(date);
            for (Event event : eventsOnDate) {
                String startTime = event.startTime;
                String endTime = event.endTime;
                int startHour;
                int startMinute;
                int start;
                int endHour;
                int endMinute;
                int end;
                if (startTime.length() == 4) {
                    startHour = Integer.parseInt(String.valueOf(startTime.charAt(0)));
                    startMinute = (Integer.parseInt(startTime.substring(2))) / 60;
                    start = startHour + startMinute;
                } else {
                    startHour = Integer.parseInt(startTime.substring(0, 2));
                    startMinute = (Integer.parseInt(startTime.substring(3))) / 60;
                    start = startHour + startMinute;
                }

                if (endTime.length() == 4) {
                    endHour = Integer.parseInt(String.valueOf(endTime.charAt(0)));
                    endMinute = (Integer.parseInt(endTime.substring(2))) / 60;
                    end = endHour + endMinute;
                } else {
                    endHour = Integer.parseInt(endTime.substring(0, 2));
                    endMinute = (Integer.parseInt(endTime.substring(3))) / 60;
                    end = endHour + endMinute;
                }

                System.out.println("Start Int: " + start + ", End Time: " + end);
                int eventStartY = eventSpacing + (50 * start);
                g.setColor(Color.pink);
                g.fillRect(55, eventStartY, 2000, (50 * (end - start)));
                g.setColor(Color.black);
                g.drawString(event.eventName, 65, eventStartY + 20);
                g.drawString(event.startTime + " - " + event.endTime, 65, eventStartY + 35);
            }
        }
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
        System.out.println("Update view with date: " + date + ", setting: " + DAY_MONTH_SETTING);
        this.repaint();
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

    // TODO Create TOSTRING method

}

//view.setText("Day View: " + date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
//view.setText("Month View: " + date.getMonth() + ", " + date.getYear());
