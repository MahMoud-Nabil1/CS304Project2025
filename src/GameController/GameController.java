package GameController;

import GameObjects.PlayerCar;
import GameObjects.PowerUp;
import java.util.ArrayList;

public class GameController {
    public static int score = 0;
    public static double gameSpeed = 1;
    public static ArrayList<PowerUp> powerUpsList = new ArrayList<>();
    public int powerUpTimer= 500;
    public static boolean doubleBulletActive= false;


}
