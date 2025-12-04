package GameObjects;

public class Obstacles extends GameObject {
    int damage;
    Obstacles(int posX, int posY) {
        super(posX, posY);
        damage = 100;
    }
    Obstacles(int posX, int posY,int damage){
        super(posX, posY);
        this.damage = damage;
    }
}
