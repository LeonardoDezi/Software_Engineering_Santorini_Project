package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
/** this class represents the introductory frame shown to the user when the GUI is started */
public class IntroFrame extends JFrame {
    
    /** the name of the file containing the introductory image */
    private static final String INTRONAME = new String("INTRO.png");
    /** the name of the file containing the play button icon */
    private static final String BUTTONNAME = new String("play rettangolare.png");

    /** the label containing the introductory image */
    private JLabel imageLabel;
    /** the button used to go to the next window */
    private JButton playButton;
    


    /** gets the image assigned to the file name passed as parameter
     * @param cl is the caller's classloader
     * @param title is the name of the requested image's file
     * @return the requested image as a BufferedImage
     */
    public static BufferedImage getImage(ClassLoader cl, String title){

        URL url = cl.getResource(title);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return img;
    }

    /**
     * gets the image passed as parameter resized to the given dimensions
     * @param img is the image that we want to resize
     * @param w is the width wanted
     * @param h is the height wanted
     * @return the image resized according to w and h
     */
    public static Image getScaledImage(Image img, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }


    /** the actionListener assigned to the play button. When the button is pressed, it closes
     * the introFrame, and creates a startDialog and the mainFrame */
    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            IntroFrame.this.dispose();
            Client client = new Client("localhost", 8080);
            MainFrame frame = new MainFrame(client);
            new StartWindow(frame);
        }
    }


    /** creates the introductory frame */
    public IntroFrame(){
        super();

        ClassLoader cl = this.getClass().getClassLoader();

        setSize(740, 730);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        BufferedImage img = getImage(cl, INTRONAME);
        Image image = getScaledImage(img, this.getWidth() - 15, this.getHeight() - 38);

        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setLayout( new BorderLayout() );
        add(imageLabel);


        img = getImage(cl, BUTTONNAME);
        image = getScaledImage(img, 200, 60);


        JLayeredPane pane = this.getLayeredPane();

        playButton = new JButton();
        playButton.setDisabledIcon(new ImageIcon(image));
        playButton.setIcon(new ImageIcon(image));
        playButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playButton.addActionListener(new ButtonListener());
        playButton.setContentAreaFilled(false);

        playButton.setVisible(true);


        JPanel panel = new JPanel(new GridLayout());
        panel.add(playButton);
        panel.setSize(200,60);
        panel.setOpaque(true);
        pane.add(panel, Integer.valueOf(2));
        panel.setLocation(270, 620);

        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);


    }




}
