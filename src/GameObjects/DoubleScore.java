package GameObjects;

import GameController.GameController;

public class DoubleScore extends PowerUp {
    private int durationFrames;
    public DoubleScore(int x, int y, int durationFrames) {
        super(x, y);
        this.durationFrames = durationFrames;
    }
    public void apply(PlayerCar car) {
        car.score += 1; // activate the double bullet
    }
    public void remove(PlayerCar car) {}

    // It is already inside PowerUp.java --- Shehab Fix
    /*public void update(PlayerCar car) {
        y -= (float) GameController.gameSpeed;
        if (durationFrames > 0) {
            durationFrames--;
            if (durationFrames <= 0) remove(car);
        }
    }
    */


}
