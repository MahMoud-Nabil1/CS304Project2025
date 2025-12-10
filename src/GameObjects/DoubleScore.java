package GameObjects;

import GameController.GameController;

public class DoubleScore extends PowerUp {

    public

    DoubleScore(float x, float y, int durationFrames) {
        super(x, y);
        // FIX: Assign to the PARENT's durationFrames.
        // Do NOT declare "private int durationFrames" here.
        this.durationFrames = durationFrames;
    }

    @Override
    public void apply(PlayerCar car) {
        isCollected = true; // Tells the parent update() to start the timer

        // Activate the logic
        // You need to create this boolean in your GameController!
        GameController.doubleScoreActive = true;

        // Hide the visual sprite, but keep the object "alive" so the timer runs
        setPosY(-100);
    }

    @Override
    public void remove(PlayerCar car) {
        // Deactivate the logic when time is up
        GameController.doubleScoreActive = false;
    }
}
