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

        // CRITICAL: Set specific size for Bullet
        // If you don't do this, the bullet will have a huge hit-box (50x100)
        this.width = 10;
        this.height = 20;
    }
}
