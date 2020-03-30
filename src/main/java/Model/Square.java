package Model;

public class Square {

    private int value;
    private final int x;
    private final int y;
    private int level;
    private Builder builder;

    public Square(int i, int j) {
        x = i;
        y = j;
        this.level = 0;
        this.builder = null;
        this.value = 0;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue(){ return this.value;}

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return this.level;
    }

    public void setBuilder(Builder builder){
        this.builder = builder;
    }

    public Builder getBuilder(){
        return this.builder;
    }


}
