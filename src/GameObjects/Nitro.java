package GameObjects;

public class Nitro extends PowerUp {

    public Nitro(float x, float y) {
        super(x, y);
    }

    @Override
    public void apply(PlayerCar car) {
        isCollected = true;

        // 1. ADD 100 (Half of the 200 max)
        car.nitro += 100;

        // 2. CAP AT MAX (Ensure it doesn't go over 200)
        // If you take 2 Nitros: 0 -> 100 -> 200.
        // If you take a 3rd one, it stays at 200.
        if (car.nitro > 200) {
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