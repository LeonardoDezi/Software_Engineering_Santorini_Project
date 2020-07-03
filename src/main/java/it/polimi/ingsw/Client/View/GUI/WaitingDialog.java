package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class WaitingDialog extends JFrame{

    private JPanel mainPanel;
    private JLabel jLabel;
    private MainFrame frame;

    private final static String newline = "\n";



    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            WaitingDialog.this.dispose();
        }
    }


    public WaitingDialog(MainFrame frame) {
        super( "Waiting");
        this.frame = frame;

        setResizable(false);


        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    IntroFrame.exit(frame);
                } catch (IOException ioException) {
                    System.exit(1);
                }
            }
        };
        this.addWindowListener(exitListener);



        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        JLabel title = new JLabel("Waiting for Players");
        mainPanel.add(title, BorderLayout.PAGE_START);

        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(300, 30));
        setLocationRelativeTo(null);


    }
}
