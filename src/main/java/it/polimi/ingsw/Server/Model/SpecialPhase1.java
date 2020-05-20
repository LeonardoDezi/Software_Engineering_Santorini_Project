package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase1 {

    //aggiungere gli if in turnManager

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder builder;
    private Square position;
    private ArrayList<Square> possibleMoves;
    private Player player;



    public SpecialPhase1(Game game){
        basicRules = game.getRules();
        this.game = game;
        this.board = game.getBoard();
        map();
    }


    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();
    //getMoves
        movesCommands.put("additionalBuild", () -> {possibleMoves = basicRules.getBuildingRange(builder);}); //Prometeo
        movesCommands.put("oppositeSideMoves", this::oppositeSideMoves);  //Caronte
        movesCommands.put(null, () ->{possibleMoves = new ArrayList<>();});
        movesCommands.put("restore", this::restore);  //Athena

    //actionMethod
        actionCommands.put(null, ()->{});  //forse non serve
        actionCommands.put("specialBuild", this::specialBuild);  //Prometeo
        actionCommands.put("moveOpposite", this::moveOpposite);  //Caronte
    }


    public ArrayList<Square> getMoves (Player player, Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)
        this.player = player;   //rivedere
        this.builder = builder;
        movesCommands.get(player.getCard().parameters.specialPhase1Moves).run();
        return possibleMoves;
    }


    public void oppositeSideMoves(){

        possibleMoves = basicRules.proximity(builder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);
            Builder opponentBuilder = position.getBuilder();

            //al posto di getValue() controlliamo getBuilder() ma prima vorrei fare più test sugli square
            if(position.getValue() == 1 && !(opponentBuilder.getColour().equals(builder.getColour()))){

                    int builderX = builder.getPosition().x;
                    int builderY = builder.getPosition().y;

                    int positionX = position.x;
                    int positionY = position.y;

                    int a = 2 * builderX - positionX;
                    int b = 2 * builderY - positionY;

                    try{
                        if(board.fullMap[a][b].getValue() != 0) {
                            possibleMoves.remove(i);
                            i--;
                        }
                    }catch(ArrayIndexOutOfBoundsException e){   //controllare che sia l'exception giusto'
                        possibleMoves.remove(i);
                        i--;
                    }

            }else{
                possibleMoves.remove(i);
                i--;
            }
        }

    }

    public void restore(){   //se athena aveva modificato maxHeight questo lo ristabilirà
        basicRules.setMaxHeight(1);
        possibleMoves = new ArrayList<>();
    }




    public void actionMethod(Builder builder, Square position){
        this.builder = builder;
        this.position = position;
        actionCommands.get(player.getCard().parameters.specialPhase1Action).run();
    }

    public void specialBuild(){
        basicRules.build(player, position, false);
        basicRules.setMaxHeight(0);
    }

    public void moveOpposite(){

        int builderX = builder.getPosition().x;
        int builderY = builder.getPosition().y;

        int positionX = position.x;
        int positionY = position.y;

        int a = 2 * builderX - positionX;
        int b = 2 * builderY - positionY;

        Square destination = board.fullMap[a][b];
        board.move(position, destination);

    }






}
