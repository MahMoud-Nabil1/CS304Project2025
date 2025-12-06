package GameObjects;

public class Obstacles extends GameObject {
    public int damage;
    public int timer=1000;
    public Obstacles(int posX, int posY) {
        super(posX, posY);
        damage = 100;

        this.width = 8;
        this.height = 2;
    }
    Obstacles(int posX, int posY,int damage){
        super(posX, posY);
        this.damage = damage;

        // Set size here too
        this.width = 8;
        this.height = 2;
    }


    @Override
    public void takeDamage(int amount) {
        // Do nothing. I am a rock. I cannot die.
    }

}
