package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingDialog extends JFrame{
    //private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JLabel jLabel;

    private final static String newline = "\n";




    //private View view;

    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            WaitingDialog.this.dispose();
        }
    }


    public WaitingDialog() {
        super( "Waiting");

        //this.view = view;

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
