package GameObjects;

public class LightCar extends Car{
    protected int nitro=100;
    LightCar(int posX, int posY) {
        super(posX,posY,100,20,100);
    }
    LightCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);

    }
}
