package GameObjects;

public class HeavyCar extends Car{
    HeavyCar(int posX, int posY) {
        super(posX,posY,50,50,200);
        // Heavy cars are slightly bigger and tougher
        this.width = 70;
        this.height = 110;
    }
    HeavyCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        this.width = 70;
        this.height = 110;
    }
}
