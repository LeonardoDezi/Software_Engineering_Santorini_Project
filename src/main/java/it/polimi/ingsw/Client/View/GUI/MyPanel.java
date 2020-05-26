package it.polimi.ingsw.Client.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

class MyPanel extends JPanel {

    private int squareX = 50;
    private int squareY = 50;
    private int squareW = 20;
    private int squareH = 20;

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(), e.getY());
            }
        });


    }

    private void moveSquare(int x, int y) {
        int OFFSET = 1;
        if ((squareX != x) || (squareY != y)) {
            repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
            squareX = x;
            squareY = y;
            repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
        }
    }


    public Dimension getPreferredSize() {
        return new Dimension(650, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
        g.fillRect(squareX,squareY,squareW,squareH);
        g.setColor(Color.BLACK);
        g.drawRect(squareX,squareY,squareW,squareH);

        g.setColor(Color.GREEN);
        g.fillRect(squareX+20,squareY+20,squareW,squareH);
        g.setColor(Color.BLACK);
        g.drawRect(squareX+20,squareY+20,squareW,squareH);  */

        //drawCards(g);
        myDrawImage(g);

    }


    private void drawCards(Graphics g) {
        ClassLoader cl = this.getClass().getClassLoader();
        String[] cardNames = new String[]{"_0000s_0006_god_and_hero_powers0052", "_0000s_0007_god_and_hero_cards_0049_Odysseus"};
        int x = 10;
        int y = 30;
        for (String item : cardNames) {
            InputStream url = cl.getResourceAsStream(item + ".png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            int W = img.getWidth();
            int H = img.getHeight();
            g.drawImage(img, x, y, W / 2, H / 2, null);
            x += W / 2 + 10;

        }
    }
    private void myDrawImage(Graphics g) {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream("SantoriniBoard.png");
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        g.drawImage(img, 10, 20, 300, 300, null);
    }
}
