package GameController;

public class GameController {
    public static int score = 0;
    public static double gameSpeed = 1;

    public static void startGame() {
        System.out.println("Game started!");

    }

    public static void setGameSpeed(double gameSpeed) {
        GameController.gameSpeed = gameSpeed;
    }
}
