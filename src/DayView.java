import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DayView extends JComponent {

    LocalDate date;

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
            g.drawLine(50, spacing-5, 2000, spacing-5);
            time++;
            spacing += 50;
        }
        // TODO FOR LOOP

        // TODO RENDER EVENTS METTHOD CALL
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        date = newDate;
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
