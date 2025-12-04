import com.sun.opengl.util.FPSAnimator;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GLCanvas;
import javax.swing.*;

public class CarGame extends JFrame  {
    GLCanvas glcanvas;
    private final FPSAnimator animator;
    private final Map<String, Runnable> buttonActions = new HashMap<>();
    public static void main(String[] args) {
        new CarGame().animator.start();
    }

    public CarGame() {
        super("GameObjects.Car Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        glcanvas = new GLCanvas();
        CarGLEventListener listener = new CarGLEventListener();
        glcanvas.addGLEventListener(listener);
        animator = new FPSAnimator(glcanvas, 60);
        add(glcanvas, BorderLayout.CENTER);
        glcanvas.setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(this);
        setVisible(true);
        glcanvas.addKeyListener(listener);
        glcanvas.setFocusable(true);
        glcanvas.requestFocus();
    }
}
