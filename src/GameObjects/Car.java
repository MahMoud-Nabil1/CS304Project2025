package GameObjects;

public class Car extends GameObject {
    protected int speed;
    protected int damage;
    protected int health;
    protected  double damageFactor;
    Car(int posX,int posY,int speed, int damage, int health) {
        super(posX, posY);
        this.speed = speed;
        this.damage = damage;
        this.health = health;
        this.damageFactor = 1;
    }
}

