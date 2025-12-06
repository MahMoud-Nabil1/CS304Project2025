package GameObjects;

public class Rocket extends GameObject
{
    int speed;
    int damage;
    public Rocket(int posX, int posY){
        super(posX, posY);
        speed = 150;
        damage = 200;

    }
}
