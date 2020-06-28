package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.PAGE_END;

public class FatalErrorWindow extends JFrame {


        /** the name of the file containing the background image */
        protected static final String IMAGENAME = new String("startDialog.png");
        /** the name of the file containing the start button icon */
        protected static final String BUTTONNAME = new String("Ok.png");






        /** the actionListener assigned to the start button. When the button is pressed, it closes
         * the startDialog, sends the username to the server and creates a waitingDialog */
        private class ConfirmListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                FatalErrorWindow.this.dispose();
                System.exit(-1);
            }
        }


    /*
    1) creare il JLabel con l'immagine di sfondo
    2) aggiungerlo al frame/dialog con un semplice add
    3) prendere il JLayeredPane
    4) Fare il pannello che conterrà i componenti
    5) setSize() del pannello che conterrà i componenti
    5) inserire i componenti nel pannello
    6) aggiungere il pannello con pane.add() e Integer.valueOf(2)
     */



        public FatalErrorWindow() {
            super();


            setResizable(false);


            ClassLoader cl = this.getClass().getClassLoader();


            Image img = IntroFrame.getImage(cl, IMAGENAME);
            img = IntroFrame.getScaledImage(img, 300, 300);
            JLabel imageLabel = new JLabel(new ImageIcon(img));


            add(imageLabel);

            JLayeredPane pane = this.getLayeredPane();

            JPanel mainPanel = new JPanel();
            mainPanel.setSize(300,300);
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new BorderLayout(50, 50));  //50
            mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 20, 40, 20));


            img = IntroFrame.getImage(cl, BUTTONNAME);
            img = IntroFrame.getScaledImage(img, 110, 45);

            JButton confirmButton = new JButton();
            confirmButton.setDisabledIcon(new ImageIcon(img));
            confirmButton.setIcon(new ImageIcon(img));
            confirmButton.setContentAreaFilled(false);
            confirmButton.setBorderPainted(false);
            confirmButton.addActionListener(new ConfirmListener());
            mainPanel.add(confirmButton, PAGE_END);


            JTextArea textField = new JTextArea();
            textField.setOpaque(false);
            textField.setEditable(false);
            textField.setLineWrap(true);
            textField.setWrapStyleWord(true);
            textField.setFont(new Font("Diogenes", Font.BOLD, 14));
            textField.setText("FATAL ERROR: impossibile proseguire la partita");
            mainPanel.add(textField, BorderLayout.CENTER);

            pane.add(mainPanel, Integer.valueOf(2));
            pack();
            setLocationRelativeTo(null);

            setVisible(true);



        }

        public static void main(String[] args) {
            new FatalErrorWindow();
        }

    }





