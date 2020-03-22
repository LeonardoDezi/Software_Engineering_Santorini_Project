package Model;

public class Square {
    private int value;
    private final Coordinates coordinates;
    private boolean isDome;
    private int level;

    public Square(int i, int j) {
        coordinates = new Coordinates(i, j);
        if(i==6||i==0 || j==6 || j==0){
            this.level = 5;  // il recinto di cupole
            this.value = 3; // 3 per indicare la cupola? TBD
            this.isDome = true; // questo lo vogliamo togliere alla fine?
        }else{
            this.value = 0;   //ho messo value=0 come casella libera per il momento TBD
            this.level = 0;
            this.isDome = false;
        }
    }

    public void setValue(int value) {
        this.value = value;
    }
}
