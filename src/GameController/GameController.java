package GameController;

import GameObjects.LightCar;
import GameObjects.Obstacles;
import GameObjects.PlayerCar;
import GameObjects.PowerUp;

import javax.swing.*;
import java.util.ArrayList;

public class GameController {
    public static int score = 0;
    public static double gameSpeed = 1;
    static String UserName;
    public static ArrayList<PowerUp> powerUpsList = new ArrayList<>();
    public int powerUpTimer= 500;
    public static boolean doubleBulletActive= false;
    public static boolean doubleScoreActive = false;
    public static ArrayList<LightCar> LightCars = new ArrayList<>();
    public static ArrayList<Obstacles> obstaclesList = new ArrayList<>();



    public static void TakeUserName() {
        while (UserName == null || UserName.trim().isEmpty()) {
            UserName = JOptionPane.showInputDialog(null, "Please enter your name (Required):");
            if (UserName == null || UserName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must enter a name to play!");
            }
        }

        System.out.println("User entered: " + UserName);
    }



}
