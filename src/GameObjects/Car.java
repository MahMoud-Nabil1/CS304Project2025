package GameObjects;

public class Car extends GameObject {
    protected double speed;
    protected int damage;
    protected int health;
    protected  double damageFactor;
    Car(double posX, double posY, double speed, int damage, int health) {
        super(posX, posY);
        this.speed = speed;
        this.damage = damage;
        this.health = health;
        this.damageFactor = 1;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}

