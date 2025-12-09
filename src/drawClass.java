import GameObjects.*;
import javax.media.opengl.GL;
import GameController.GameController;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static GameController.GameController.obstaclesList;
import static GameController.TextureHandling.powerUpTextures;
import static GameController.TextureHandling.textures;

public class drawClass {
    static int maxWidth = 100;
    static int maxHeight = 100;
    static int[] obstaclesPositions = {11, 29, 45, 62, 79};
    static double roadOffsetY = 0.0f;
    static int PowerUPTimer = 0;
    static final int MAX_CARS_ON_SCREEN = 3;
    static final int MAX_OBSTACLES_ON_SCREEN = 2;
    static final float CAR_BUFFER_DISTANCE = 60f;

    static float[] lastCarSpawnY = new float[obstaclesPositions.length];
    static boolean[] laneHasObstacle = new boolean[obstaclesPositions.length];



    private static void drawStandardQuad(GL gl) {
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
    }

    public static void DrawBackground(GL gl, int index, int[] textures){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        // No translation needed for background usually, but kept your logic
        drawStandardQuad(gl); // <--- Reused code
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public static void background_loop(GL gl , int[] textures) {
        roadOffsetY -= 0.02f * GameController.gameSpeed;
        if (roadOffsetY <= -2.0f) {
            roadOffsetY = 0.0f;
        }
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

        // Tile 1
        gl.glPushMatrix();
        gl.glTranslated(0.0f, roadOffsetY, 0.0f);
        drawStandardQuad(gl);
        gl.glPopMatrix();

        // Tile 2
        gl.glPushMatrix();
        gl.glTranslated(0.0f, roadOffsetY + 2.0f, 0.0f);
        drawStandardQuad(gl);
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public static void DrawSpriteNoRotation(GL gl, float x, float y, int index, float scale, int[] textures){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated(x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        drawStandardQuad(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public static void drawSprite(GL gl, float x, float y, int index, float scale , int[] textures){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated(x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        gl.glRotated(CarGLEventListener.angle, 0, 0, 1);
        drawStandardQuad(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public static void drawBullets(GL gl, PlayerCar player, int[] textures) {
        Iterator<Bullet> iter = player.bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            if (bullet.timer >= 0) {
                DrawSpriteNoRotation(gl, (float) bullet.posX, (float) (bullet.posY + 10), 8, 1.0f, textures);

                // Move it
                bullet.posY += 2 + GameController.gameSpeed;

                // Decrease life
                bullet.timer--;
            } else {
                iter.remove();
            }
        }

        // Handle Fire Rate Cooldown
        if (player.firerate > 0) {
            player.firerate--;
        }
    }

    public static void drawPowerUps(GL gl, PlayerCar player) {
        if (GameController.powerUpsList.size() < 6 && PowerUPTimer <= 0) {
            powerUpsSpawn();
            PowerUPTimer = 50;
        }
        player.update();
        for (int i = GameController.powerUpsList.size() - 1; i >= 0; i--) {
            PowerUp p = GameController.powerUpsList.get(i);
            p.update(player);

            if (!p.isCollected) {
                int baseIndex = 0;
                if (p instanceof Nitro) baseIndex = 0;
                else if (p instanceof Repair) baseIndex = 6;
                else if (p instanceof DoubleBullets) baseIndex = 12;
                else if (p instanceof DoubleScore) baseIndex = 18;

                int animationOffset = (CarGLEventListener.frameCounter / 3) % 6;
                int finalTexIndex = baseIndex + animationOffset;

                DrawSpriteNoRotation(gl, (float) p.getPosX(), (float) p.getPosY(), finalTexIndex, 0.7f, powerUpTextures);

                if (p.getPosY() <= -50) {
                    GameController.powerUpsList.remove(i);
                }
            }
        }
        PowerUPTimer--;
    }

    public static void powerUpsSpawn() {
        float minX = 15; float maxX = 85;
        float spawnX = minX + (float)(Math.random() * ((maxX - minX) + 1));
        int randomizer = (int) (Math.random()*4);
        int duration = 300;

        PowerUp p = null;
        switch (randomizer) {
            case 0: p = new Nitro(spawnX, 100); break;
            case 1: p = new Repair(spawnX, 100); break;
            case 2: p = new DoubleBullets(spawnX, 100, duration); break;
            case 3: p = new DoubleScore((int)spawnX, 100, duration); break;
        }

        if (p != null) {
            GameController.powerUpsList.add(p);
            CarGLEventListener.allObjects.add(p);
        }
    }

    public static void drawHitboxDebug(GL gl, Rectangle rect) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glPushMatrix();

        double x = rect.x / 50.0 - 1.0;
        double y = rect.y / 50.0 - 1.0;
        double w = rect.width / 50.0;
        double h = rect.height / 50.0;

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + w, y);
        gl.glVertex2d(x + w, y + h);
        gl.glVertex2d(x, y + h);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    public static void initGameLogic() {
        Arrays.fill(lastCarSpawnY, -1000);
        Arrays.fill(laneHasObstacle, false);
        obstaclesList.clear();
        GameController.LightCars.clear();
        GameController.HeavyCars.clear();
    }

    public static void renderAndLogic(GL gl, int[] textures) {
        handleSpawningLogic();
        drawAndMoveObstacles(gl, textures);
        drawAndMoveCars(gl, textures);
    }

    private static void handleSpawningLogic() {
        int currentTotalCars = GameController.LightCars.size() + GameController.HeavyCars.size();
        int currentTotalObs = obstaclesList.size();

        if (currentTotalObs < MAX_OBSTACLES_ON_SCREEN) {
            trySpawnEntity(true);
        }

        if (currentTotalCars < MAX_CARS_ON_SCREEN) {
            trySpawnEntity(false);
        }
    }

    private static void trySpawnEntity(boolean isObstacle) {
        List<Integer> lanes = new ArrayList<>();
        for (int i = 0; i < obstaclesPositions.length; i++) lanes.add(i);
        Collections.shuffle(lanes);

        for (int laneIndex : lanes) {
            if (laneHasObstacle[laneIndex]) continue;

            if (!isObstacle) {
                if (100 - lastCarSpawnY[laneIndex] < CAR_BUFFER_DISTANCE) continue;
            }

            if (!isMapPassableIfWeBlock(laneIndex)) continue;

            if (isObstacle) {
                performSpawnObstacle(laneIndex);
            } else {
                performSpawnCar(laneIndex);
            }
            return;
        }
    }

    private static boolean isMapPassableIfWeBlock(int candidateLane) {
        int blockedLanesCount = 0;

        for (int i = 0; i < obstaclesPositions.length; i++) {
            boolean isBlocked = false;

            if (laneHasObstacle[i]) isBlocked = true;
            if (i == candidateLane) isBlocked = true;
            if (lastCarSpawnY[i] > 80) isBlocked = true;

            if (isBlocked) blockedLanesCount++;
        }

        return blockedLanesCount < obstaclesPositions.length;
    }

    private static void performSpawnObstacle(int lane) {
        float x = obstaclesPositions[lane];
        Obstacles o = new Obstacles(x, 100);
        obstaclesList.add(o);
        CarGLEventListener.allObjects.add(o);
        laneHasObstacle[lane] = true;
    }

    private static void performSpawnCar(int lane) {
        float x = obstaclesPositions[lane];
        boolean isLight = Math.random() < 0.5;

        if (isLight) {
            LightCar c = new LightCar(x, 100);
            GameController.LightCars.add(c);
            CarGLEventListener.allObjects.add(c);
        } else {
            HeavyCar c = new HeavyCar(x, 100);
            GameController.HeavyCars.add(c);
            CarGLEventListener.allObjects.add(c);
        }

        lastCarSpawnY[lane] = 100;
    }

    public static void drawAndMoveObstacles(GL gl, int[] textures) {
        Iterator<Obstacles> iter = obstaclesList.iterator();
        while (iter.hasNext()) {
            Obstacles obs = iter.next();
            DrawSpriteNoRotation(gl, (float) obs.getPosX(), (float) obs.getPosY(), 7, 1.4f, textures);

            obs.posY -= (float) GameController.gameSpeed;

            if (obs.getPosY() < -10) {
                int lane = indexOfLane(obs.getPosX());
                if (lane != -1) laneHasObstacle[lane] = false;

                iter.remove();
                CarGLEventListener.allObjects.remove(obs);
            }
        }
    }

    public static void drawAndMoveCars(GL gl, int[] textures) {
        Iterator<LightCar> iterLight = GameController.LightCars.iterator();
        while (iterLight.hasNext()) {
            LightCar car = iterLight.next();
            DrawSpriteNoRotation(gl, car.getPosX(), car.getPosY(), 14, 1.4f, textures);
            updateCarPhysics(car, iterLight);
        }

        Iterator<HeavyCar> iterHeavy = GameController.HeavyCars.iterator();
        while (iterHeavy.hasNext()) {
            HeavyCar car = iterHeavy.next();
            DrawSpriteNoRotation(gl, car.getPosX(), car.getPosY(), 17, 1.7f, textures);
            updateCarPhysics(car, iterHeavy);
        }
    }

    private static void updateCarPhysics(Object carObj, Iterator<?> iter) {
        float posX, posY, speed;
        if (carObj instanceof LightCar) {
            LightCar c = (LightCar) carObj;
            posX = c.getPosX(); posY = c.getPosY(); speed = (float) c.getSpeed();
            c.posY -= (GameController.gameSpeed + speed);
            int lane = indexOfLane(posX);
            if (lane != -1) lastCarSpawnY[lane] = c.getPosY();
        } else {
            HeavyCar c = (HeavyCar) carObj;
            posX = c.getPosX(); posY = c.getPosY(); speed = (float) c.getSpeed();
            c.posY -= (float)(GameController.gameSpeed + speed);
            int lane = indexOfLane(posX);
            if (lane != -1) lastCarSpawnY[lane] = c.getPosY();
        }

        if (posY < -10) {
            iter.remove();
            CarGLEventListener.allObjects.remove(carObj);
        }
    }

    private static int indexOfLane(float x) {
        for (int i = 0; i < obstaclesPositions.length; i++) {
            if (Math.abs(obstaclesPositions[i] - x) < 0.01f) return i;
        }
        return -1;
    }




























}