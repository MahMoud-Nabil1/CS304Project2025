package GameObjects;

public class Repair extends PowerUp {
    public Repair(float x, float y) {
        super(x, y);
    }
    @Override
    public void apply(PlayerCar car) {
        if (car.health<=50) {
            car.health+=50;
        }
        else
            car.health=100;
    }
    @Override
    public void remove(PlayerCar car) {}
}
