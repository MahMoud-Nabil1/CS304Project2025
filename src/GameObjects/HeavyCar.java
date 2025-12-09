package GameObjects;

public class HeavyCar extends Car{
    public HeavyCar(float posX, float posY) {
        super(posX,posY,.3,50,200);
        // Heavy cars are slightly bigger and tougher
        this.width = 4;
        this.height = 10;
    }
    HeavyCar(float posX, float posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        this.width = 4;
        this.height = 10;
    }
}
