package GameObjects;

public class Bullet extends GameObject {
    public Bullet(int posX,int posY,int speed, int damage) {
        super(posX,posY);
        this.posX = posX;
        this.posY = posY;
    }
}
