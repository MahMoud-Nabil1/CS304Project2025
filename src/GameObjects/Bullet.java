package GameObjects;

public class Bullet extends GameObject {
    int speed;
    public int damage;
    public int timer=1000;
    public Bullet(double posX,double posY,int damage) {
        super(posX,posY);
        this.posX = posX;
        this.posY = posY;
        this.speed = 20;
        this.damage = damage;
    }
}
