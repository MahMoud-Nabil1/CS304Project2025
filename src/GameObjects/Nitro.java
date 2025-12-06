package GameObjects;

public class Nitro extends PowerUp {

    public Nitro(float x, float y) {
        super(x, y);
    }

    @Override
    public void apply(PlayerCar car) {
        isCollected = true;

        // Apply logic
        if (car.nitro <= 100) {
            car.nitro += 100;
        } else {
            car.nitro = 200;
        }

        // Instant item: kill it immediately
        alive = false;
    }

    @Override
    public void remove(PlayerCar car) {
        // Nothing to remove for Nitro
    }
}