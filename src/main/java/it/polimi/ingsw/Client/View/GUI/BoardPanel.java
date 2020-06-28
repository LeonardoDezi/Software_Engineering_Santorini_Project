package it.polimi.ingsw.Client.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BoardPanel extends JPanel {

    private BufferedImage img;

    public BoardPanel(){

        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource("SantoriniBoard.png");
        img = null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {

            System.out.println("Nope.");
        }



    }




    public Dimension getPreferredSize() {
        return new Dimension(653, 653);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0 ,0, 683, 683, this);   //683
        //myDrawImage(g);
    }


}
