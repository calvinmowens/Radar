import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Radar {

    public static void main(String[] args) {

        JFrame myFrame = new JFrame();

        JButton start = new JButton("start");
        start.setBounds(10, 10, 100, 100);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start Pressed!!");
            }
        });

        myFrame.add(start);

        myFrame.setSize(400, 500);
        myFrame.setLayout(null);
        myFrame.setVisible(true);

    }

}
