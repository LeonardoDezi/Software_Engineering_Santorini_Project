package Model;

public class Coordinates {
    private final int x;   // o public o protected o friendly
    private final int y;

    public Coordinates(int i, int j){
        this.x= i;
        this.y =j;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
