package Model;

public class Square<coordinates> {
    private int value;
    private final coordinates coordinates;
    private boolean isdome;
    private int level;

    public Square(coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
