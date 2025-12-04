package GameObjects;

public class HeavytCar extends Car{
    HeavytCar(int posX,int posY) {
        super(posX,posY,50,50,200);
    }
    HeavytCar(int posX,int posY,int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
    }
}
