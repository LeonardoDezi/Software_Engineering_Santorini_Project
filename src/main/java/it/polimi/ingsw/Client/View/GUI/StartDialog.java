package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.ClientApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartDialog extends JDialog {

    private static final long serialVersionUID = 1L;  //?

    private JPanel mainPanel;
    private JTextField textField;
    private JTextArea textArea;
    private final static String newline = "\n";
    //private JComboBox<Choice> choices;
    private JButton confirmButton;
    private JFrame frame;
    private String text;




    //private View view;

    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            try {
                ClientApp.main(text);
            } catch (IOException ioException) {    //TODO risolvere questo problema
                ioException.printStackTrace();
            }
            StartDialog.this.dispose();
            new PlayerNumberDialog(frame);
        }
    }

    private class TextListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            text = textField.getText();
        }
    }

    public StartDialog(JFrame frame/*, View view*/) {
        super(frame, "Player choice");

        //this.view = view;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        textField = new JTextField(20);
        textField.addActionListener(new TextListener());
        mainPanel.add(textField, BorderLayout.CENTER);

        JLabel title = new JLabel(" Benvenuto! Inserisci il tuo username");
        mainPanel.add(title, BorderLayout.PAGE_START);

        confirmButton = new JButton("Start!!");
        confirmButton.addActionListener(new ConfirmListener());
        mainPanel.add(confirmButton, BorderLayout.PAGE_END);

        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(300, 30));
        setVisible(true);

    }
}


