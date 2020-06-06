package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

 /**
  * this class is used to test the behaviour of MovementPhase
  */
 public class BuildingPhaseTest {
     /** the object tested */
     BuildingPhase buildingPhase;
     /** the object tested */
     BuildingPhase buildingPhase1;
     /** the two-player game used for the test */
     Game game;
     /** the three-player game used for the test */
     Game game1;
     /** the player used for the test*/
     Player player1;
     /** the player used for the test*/
     Player player2;
     /** the player used for the test*/
     Player player3;
     /** the player used for the test*/
     Player player4;
     /** the player used for the test*/
     Player player5;
     /** the ArrayList of Squares used for the test*/
     ArrayList<Square> possibleMoves;
     /** the context used for the test*/
     private Context context = new Context(new NetInterface(game));
     /** the context used for the test*/
     private Context context1 = new Context(new NetInterface(game1));
     private NetInterface netInterface = new NetInterface(game);



     /**
      * creates the players and the games used in every test
      */
     @Before
     public void create(){
         game = new Game(2, netInterface);
         game1 = new Game(3, netInterface);

         player1 = new Player("Marco", "Red", game, 0);
         player2 = new Player("Luca", "Blue", game, 1);

         player3 = new Player("Fra", "Black", game1, 2);
         player4 = new Player("Luca", "Blue", game1, 1);
         player5 = new Player("Marco", "Red", game1, 0);
         game.addPlayer(player1);
         game.addPlayer(player2);

         game1.addPlayer(player3);
         game1.addPlayer(player4);
         game1.addPlayer(player5);
     }

     /**
      * tests the behaviour of buildingPhase when the card has no special effect regarding BuildingPhase
      */
     @Test
     public void checkNull() throws IOException {

         player1.setCard(game.getDeckCard(6)); //Hephaestus
         player2.setCard(game.getDeckCard(14)); //Zeus

         game.gameBoard.build(game.gameBoard.fullMap[1][3], false); //torre di un blocco

         game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto: pedina su torre

         game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
         game.gameBoard.build(game.gameBoard.fullMap[0][3], false);  //torre completa
         game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
         game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
         game.deployBuilder(player1, game.gameBoard.fullMap[2][3]);   //altra pedina

         game.gameBoard.build(game.gameBoard.fullMap[1][2], false);   //torre di due
         game.gameBoard.build(game.gameBoard.fullMap[1][2], false);

         game.gameBoard.build(game.gameBoard.fullMap[2][2], true);    //cupola

         buildingPhase = new BuildingPhase(game, context, player1, player1.getBuilder(0));
         possibleMoves = buildingPhase.getMoves(player1.getBuilder(0));
         assertEquals(5, possibleMoves.size());  // si accerta della dimensione di possibleMoves

         assertEquals(2, game.gameBoard.fullMap[1][2].getLevel());
         buildingPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[1][2], false);  //generica costruzione
         assertEquals(3, game.gameBoard.fullMap[1][2].getLevel());
     }



     /**
      * tests the behaviour of buildingPhase when the card is Zeus
      */
     @Test
     public void checkZeusBehaviour() throws IOException {

         player3.setCard(game1.getDeckCard(1));  //Apollo
         player4.setCard(game1.getDeckCard(14));   //Zeus
         player5.setCard(game1.getDeckCard(12));//Hestia


         //Test 1: Il worker costruisce sotto di esso
         game1.deployBuilder(player4, game1.gameBoard.fullMap[0][0]);  // soggetto: il giocatore si trova a terra

         //pedina di giocatore avversario
         game1.deployBuilder(player5, game1.gameBoard.fullMap[0][1]);

         game1.gameBoard.build(game1.gameBoard.fullMap[1][0], false);
         game1.gameBoard.build(game1.gameBoard.fullMap[1][0], false);  //torre di due piani

         buildingPhase1 = new BuildingPhase(game1, context1, player4, player4.getBuilder(0));
         possibleMoves = buildingPhase1.getMoves(player4.getBuilder(0));
         assertEquals(3, possibleMoves.size());  //si accerta che il giocatore ha tre mosse disponibili

         assertEquals(player4.getBuilder(0).getPosition(), possibleMoves.get(possibleMoves.size() -1));  //controlla che la posizione del worker sia presente


         Builder builder = player4.getBuilder(0);
         Square position = game1.gameBoard.fullMap[0][0];
         int level = builder.getPosition().getLevel();  //livello della posizione del worker prima della costruzione
         buildingPhase1.actionMethod(builder, builder.getPosition(), false);   //costruisce nella sua posizione
         assertEquals(level +1, position.getLevel());  //si assicura che il level sia aumentato
         assertEquals(position, builder.getPosition()); //si accerta che il worker sia ancora associato al medesimo square
         assertEquals(1, position.getValue()); //si accerta che lo square indichi che c'è una pedina

         //Test 2: costruzione normale
         position = game1.gameBoard.fullMap[1][0];
         level = position.getLevel();

         buildingPhase1.actionMethod(builder, position, false);
         assertEquals(level +1, position.getLevel());

     }
}
