package GameObjects;

public class LightCar extends Car{
    protected int nitro=100;
    LightCar(int posX,int posY) {
        super(posX,posY,100,20,100);
    }
    LightCar(int posX,int posY,int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);

    }
    private boolean nitroActive = false;

    public void nitroOn() {
        if(nitro > 0 && !nitroActive) {
            this.speed += 50;
            this.damage += 50;
            damageFactor = 1.5;
            nitro--;
            nitroActive = true;
        }
        if (nitro<=0&&!nitroActive) {
            nitroOff();
        }
    }

    public void nitroOff() {
        if(nitroActive) {
            this.speed -= 50;
            this.damage -= 50;
            nitroActive = false;
        }
    }

}
