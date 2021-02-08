package com.flying.plane.screens;

import com.flying.plane.Controller;
import com.flying.plane.MyGame;
import com.flying.plane.dialogs.GameOverDialog;
import com.flying.plane.model.objects.Background;
import com.flying.plane.model.objects.FlyingPlane;
import com.flying.plane.model.objects.Ground;
import com.flying.plane.model.objects.GroundType;

public class GameScreen implements ScreenHandler {
    public final Background background;
    private final FlyingPlane plane;
    private final Controller controller;
    private final Ground ground1, ground2, ground3;
    private MyGame game;

    public GameScreen(MyGame game) {
        this.game = game;

        background = new Background();
        plane = new FlyingPlane();
        plane.setPosition(50, 350);
        controller = new Controller(this);
        controller.setPlane(plane);
        ground1 = new Ground(GroundType.DIRT);
        ground2 = new Ground(GroundType.DIRT);
        ground3 = new Ground(GroundType.ROCK);
        controller.setGround1(ground1);
        controller.setGround2(ground2);
        controller.setGround3(ground3);
    }

    @Override
    public boolean onBackPress() {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y) {
        return false;
    }

    @Override
    public boolean touchUp(float x, float y) {
        return false;
    }

    @Override
    public boolean touchDragged(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float x, float y) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count) {
        return false;
    }

    @Override
    public void show() {

        ground1.setX(0);
        ground2.setX(ground1.getX() + ground1.getWidth());
        ground3.setX(ground2.getX() + ground2.getWidth());
    }

    @Override
    public void render(float delta) {
        background.draw(game.batch);
        plane.draw(game.batch);
        ground1.draw(game.batch);
        ground2.draw(game.batch);
        ground3.draw(game.batch);
        controller.handle();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void gameOver() {
        new GameOverDialog(game).show(game.stage);
    }
}
