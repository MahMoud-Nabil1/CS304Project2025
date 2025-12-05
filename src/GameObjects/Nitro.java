package GameObjects;

public class Nitro extends PowerUp {
    public Nitro(float x, float y) {
        super(x, y);
    }
    @Override
    public void apply(PlayerCar car) {
        if (car.nitro<=50) {
            car.nitro+=50;
        }
        else
            car.nitro=100;
    }
    @Override
    public void remove(PlayerCar car) {}
}
