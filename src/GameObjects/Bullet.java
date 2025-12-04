package GameObjects;

public class Bullet extends GameObject {
    int speed;
    int damage;
    public Bullet(double posX,double posY,int damage) {
        super(posX,posY);
        this.posX = posX;
        this.posY = posY;
        this.speed = 20;
        this.damage = damage;
    }
}
