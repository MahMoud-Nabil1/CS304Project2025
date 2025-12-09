package GameController;

import Texture.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.io.IOException;

public class TextureHandling {

    static String assetsFolderName = "Assets/";

    // --- TEXTURE LISTS ---
    public static String[] textureNames = {
            "Back.png", "Playercar.png", "MenuBackGround.png", "PauseMenu.png",
            "StartButton.png", "InstructionsButton.png", "QuitButton.png", "obstacle.png",
            "bullet.png", "endBackground.png", "continuebBotton.png", "mainMenuButton.png",
            "playAgainButton.png", "pauseButton.png", "Lightcar.png", "pauseButton2.png", "loseMenu2.png"
            , "Heavycar.png"
    };

    public static String[] powerUpTextureNames = {
            "blue 1", "blue 2", "blue 3", "blue 4", "blue 5", "blue 6",
            "green 1", "green 2", "green 3", "green 4", "green 5", "green 6",
            "red 1", "red 2", "red 3", "red 4", "red 5", "red 6",
            "yellow 1", "yellow 2", "yellow 3", "yellow 4", "yellow 5", "yellow 6"
    };

    public static String[] scoreTextureNames = {
            "0.png", "1.png", "2.png", "3.png", "4.png", "5.png",
            "6.png", "7.png", "8.png", "9.png"
    };

    public static String[] healthTextureNames = {
            "HealthBar.png", "FullState1.png", "HealthReceviedFull.png",
            "3_4State1.png", "3_4State2.png", "HalfState1.png", "HalfState2.png",
            "LowState1.png", "LowState2.png"
    };

    public static String[] effectsTextureNames = {
            "nitro.png"
    };

    // --- OPENGL ID ARRAYS (We only need these!) ---
    public static int[] textures = new int[textureNames.length];
    public static int[] powerUpTextures = new int[powerUpTextureNames.length];
    public static int[] scoreTextures = new int[scoreTextureNames.length];
    public static int[] healthTextures = new int[healthTextureNames.length];
    public static int[] effectsTextures = new int[effectsTextureNames.length];


    // --- MAIN INIT METHOD ---
    public static void MainTextures(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Load Main Textures
        loadTextureBatch(gl, textureNames, textures, assetsFolderName);

        // 2. Load Score
        loadTextureBatch(gl, scoreTextureNames, scoreTextures, assetsFolderName + "//Score//");

        // 3. Load Health
        loadTextureBatch(gl, healthTextureNames, healthTextures, assetsFolderName + "//HealthBar//");
        loadTextureBatch(gl,effectsTextureNames, effectsTextures, assetsFolderName + "//Effects///");
    }

    public static void PowerUpTextures(GLAutoDrawable gld) {
        GL gl = gld.getGL();

        // Generate IDs
        gl.glGenTextures(powerUpTextureNames.length, powerUpTextures, 0);

        String[] folders = {"Blue", "Green", "Red", "Yellow"};

        for (int i = 0; i < powerUpTextureNames.length; i++) {
            // Math Magic: 0-5 = Index 0 (Blue), 6-11 = Index 1 (Green), etc.
            String colorFolder = folders[i / 6];

            String path = assetsFolderName + "//PowerUps//" + colorFolder + "//" + powerUpTextureNames[i] + ".png";

            // Reuse the single loader logic
            loadSingleTexture(gl, path, powerUpTextures, i);
        }
    }

    // --- OPTIMIZED LOADER HELPER ---
    // This removes code duplication and handles memory cleanup
    private static void loadTextureBatch(GL gl, String[] names, int[] ids, String pathPrefix) {
        gl.glGenTextures(names.length, ids, 0);

        for (int i = 0; i < names.length; i++) {
            String fullPath = pathPrefix + (pathPrefix.endsWith("//") ? "" : "//") + names[i];
            loadSingleTexture(gl, fullPath, ids, i);
        }
    }

    private static void loadSingleTexture(GL gl, String path, int[] ids, int index) {
        try {
            // 1. Read to RAM
            TextureReader.Texture tempTex = TextureReader.readTexture(path, true);

            // 2. Upload to VRAM (GPU)
            gl.glBindTexture(GL.GL_TEXTURE_2D, ids[index]);
            new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA,
                    tempTex.getWidth(), tempTex.getHeight(),
                    GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE,
                    tempTex.getPixels()
            );

            // 3. CRITICAL OPTIMIZATION:
            // The texture is now in the GPU. We do NOT save 'tempTex' to a static array.
            // Java's Garbage Collector will now delete 'tempTex' from RAM immediately.

        } catch (IOException e) {
            System.out.println("MISSING TEXTURE: " + path);
            e.printStackTrace();
        }
    }
}