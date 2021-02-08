package com.flying.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.flying.plane.model.objects.FlyingPlane;
import com.flying.plane.model.objects.Ground;
import com.flying.plane.model.objects.GroundType;
import com.flying.plane.screens.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class Controller {
    private static final float MAX_GRAVITY = -9;
    private static final float OBSTACLE_SPEED = 15;
    private boolean isRunning = false;
    private FlyingPlane plane;
    private GameScreen gameScreen;
    private float gravity = 0;
    private boolean isGameOver;
    ArrayList<Sprite> obstacleList = new ArrayList<>();
    private Ground ground1;
    private Ground ground2;
    private Ground ground3;

    public Controller(GameScreen gameScreen) {

        this.gameScreen = gameScreen;
    }

    public void setPlane(FlyingPlane plane) {

        this.plane = plane;
    }

    public void handle() {
        if (!isRunning && Gdx.input.justTouched()) {
            isRunning = true;
            plane.setStop(false);
            gameScreen.background.setSpeed(5f);
            isGameOver = false;
        }
        if (isRunning && !isGameOver) {
            handlePlaneMovement();

            handleObstacleMovement();

            handleGroundMovement();
            Rectangle rectangle = plane.getBoundingRectangle();
            if (ground1.overlap(rectangle) || ground2.overlap(rectangle) || ground3.overlap(rectangle)) {
                isGameOver = true;
                plane.setStop(true);
                gameScreen.gameOver();
            }


        }
    }

    private void handleObstacleMovement() {
        Iterator<Sprite> iterator = obstacleList.iterator();
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            sprite.setX(sprite.getX() - OBSTACLE_SPEED);

        }
    }

    private void handlePlaneMovement() {
        plane.setY(plane.getY() + gravity);

        gravity -= 1;
        if (gravity < MAX_GRAVITY)
            gravity = MAX_GRAVITY;
        if (Gdx.input.justTouched())
            gravity = 10;
    }

    private void handleGroundMovement() {
        ground1.setX(ground1.getX() - OBSTACLE_SPEED);
        ground2.setX(ground2.getX() - OBSTACLE_SPEED);
        ground3.setX(ground3.getX() - OBSTACLE_SPEED);
        if (ground1.getX() + ground1.getWidth() <= 0) {
            Ground temp = ground1;
            ground1 = ground2;
            ground2 = ground3;
            ground3 = temp;
            GroundType[] values = GroundType.values();
            ground3.updateImage(values[MathUtils.random(0, values.length - 1)]);
            ground3.setX(ground2.getX() + ground2.getWidth());
        }
    }

    public void addObjects(Sprite obstacle) {
        obstacleList.add(obstacle);
    }

    public void setGround1(Ground ground1) {

        this.ground1 = ground1;
    }

    public void setGround2(Ground ground2) {

        this.ground2 = ground2;
    }

    public void setGround3(Ground ground3) {

        this.ground3 = ground3;
    }
}
