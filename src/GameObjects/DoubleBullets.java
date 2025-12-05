package GameObjects;

import GameController.GameController;

public class DoubleBullets extends PowerUp {
    private int durationFrames;

    public DoubleBullets(float x, float y, int durationFrames) {
        super(x, y);
        this.durationFrames = durationFrames;
    }

    @Override
    public void apply(PlayerCar car) {
        GameController.doubleBulletActive = true; // activate the double bullet
    }

    @Override
    public void remove(PlayerCar car) {
        GameController.doubleBulletActive = false; // deactivate
    }

    public void update(PlayerCar car) {
        y -= (float) GameController.gameSpeed;

        if (durationFrames > 0) {
            durationFrames--;
            if (durationFrames <= 0) remove(car);
        }
    }
}
