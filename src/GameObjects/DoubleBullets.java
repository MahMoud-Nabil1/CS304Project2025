package GameObjects;

import GameController.GameController;

public class DoubleBullets extends PowerUp {

    public DoubleBullets(float x, float y, int durationFrames) {
        super(x, y);
        // FIX: Set the variable in the PARENT class, do not create a new one.
        this.durationFrames = durationFrames;
    }

    @Override
    public void apply(PlayerCar car) {
        isCollected = true; // Tell update() to stop falling and start counting
        GameController.doubleBulletActive = true;

        // Move it off-screen so we don't draw it anymore,
        // but keep it "alive" so the timer works.
        setPosY(-100);
    }

    @Override
    public void remove(PlayerCar car) {
        GameController.doubleBulletActive = false;
        System.out.println("Double Bullets Expired");
    }
}
