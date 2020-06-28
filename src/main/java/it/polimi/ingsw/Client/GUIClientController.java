package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.View.GUI.IntroFrame;
import it.polimi.ingsw.Client.View.GUI.PlayerNumberWindow;
import it.polimi.ingsw.Client.View.GUI.WaitingDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;

public class GUIClientController extends ClientController{


    private static final String INTRONAME = new String("INTRO.png");


    public GUIClientController(Client client){
        super(client);
    }

    @Override
    public void dealerChoice() throws IOException {

        ArrayList<Integer> chosenCards = this.chosenCards();
        netInterface.sendCard(chosenCards, client.getServerSocket());
    }

    @Override
    public void chooseNumberOfPlayers() throws IOException{
    /*
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PlayerNumberWindow();
            }
        });
    */



       // while(PlayerNumberWindow.getBlocked()){}

        //return PlayerNumberWindow.getNumberOfPlayers();


    }

}
