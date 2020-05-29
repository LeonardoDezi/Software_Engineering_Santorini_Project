package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PlayerNumberDialog extends JDialog {
    private static final long serialVersionUID = 1L;  //?

    private JPanel mainPanel;
    private JTextField textField;
    private JTextArea textArea;
    private final static String newline = "\n";
    //private JComboBox<Choice> choices;
    private JButton confirmButton;
    private JFrame frame;

    JRadioButton twoPlayersButton = new JRadioButton("2");
    JRadioButton threePlayersButton = new JRadioButton("3");


    //private View view;

    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PlayerNumberDialog.this.dispose();
          /*  new WaitingDialog(frame);
            new CardChoosingDialog(frame);   */
        }
    }

    private class RadioButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            e.getActionCommand();
        }
    }

    public PlayerNumberDialog(JFrame frame/*, View view*/) {
        super(frame, "Player choice");

        //this.view = view;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        twoPlayersButton.setMnemonic(KeyEvent.VK_2);
        threePlayersButton.setMnemonic(KeyEvent.VK_3);

        twoPlayersButton.setActionCommand("2");
        threePlayersButton.setActionCommand("3");

        ButtonGroup group = new ButtonGroup();
        group.add(twoPlayersButton);
        group.add(threePlayersButton);


        twoPlayersButton.addActionListener(new RadioButtonListener());
        threePlayersButton.addActionListener(new RadioButtonListener());

        JPanel radioPanel = new JPanel(new GridLayout(0,1));
        radioPanel.add(twoPlayersButton);
        radioPanel.add(threePlayersButton);
        mainPanel.add(radioPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Scegli il numero di giocatori della partita");
        mainPanel.add(title, BorderLayout.PAGE_START);

        confirmButton = new JButton("Go");
        confirmButton.addActionListener(new ConfirmListener());
        mainPanel.add(confirmButton, BorderLayout.PAGE_END);

        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(300, 30));
        setVisible(true);


    }

}
