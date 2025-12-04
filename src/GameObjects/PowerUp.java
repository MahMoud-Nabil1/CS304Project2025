package GameObjects;

public abstract class PowerUp extends GameObject{
    PlayerCar playerCar;
    PowerUp(int x, int y) {
        super(x,y);
    }
    // called when car picks it up
    public abstract void apply(PlayerCar car);

    // called when effect ends
    public abstract void remove(PlayerCar car);

}
class DoubleBullets extends PowerUp {
    private int durationFrames;

    public DoubleBullets(int x, int y, int durationFrames) {
        super(x, y);
        this.durationFrames = durationFrames;
    }

    @Override
    public void apply(PlayerCar car) {
        car.doubleBulletActive = true; // activate the double bullet
    }

    @Override
    public void remove(PlayerCar car) {
        car.doubleBulletActive = false; // deactivate
    }

    public void update(PlayerCar car) {
        if (durationFrames > 0) {
            durationFrames--;
            if (durationFrames <= 0) remove(car);
        }
    }
}
class repair extends PowerUp {
    public repair(int x, int y) {
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
class nitro extends PowerUp {
    public nitro(int x, int y) {
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
class doubleScore extends PowerUp {
    private int durationFrames;
    public doubleScore(int x, int y, int durationFrames) {
        super(x, y);
        this.durationFrames = durationFrames;
    }
    public void apply(PlayerCar car) {
        car.score += 1; // activate the double bullet
    }
    public void remove(PlayerCar car) {}
    public void update(PlayerCar car) {
        if (durationFrames > 0) {
            durationFrames--;
            if (durationFrames <= 0) remove(car);
        }
    }
}

