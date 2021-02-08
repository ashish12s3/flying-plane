package com.flying.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.flying.plane.screens.ScreenHandler;

public class InputHandler extends InputMultiplexer {
    private MyGame game;
    private Vector3 temp = new Vector3();

    public InputHandler(MyGame game) {
        this.game = game;
        addProcessor(new Gesture(game));
        addProcessor(new InpHandler());
        Gdx.input.setCatchBackKey(true);
    }

    class InpHandler extends InputAdapter {
        public boolean keyDown(int keycode) {
            return ((ScreenHandler) game.getScreen()).keyDown(keycode);
        }

        public boolean keyUp(int keycode) {
            if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
                ((ScreenHandler) game.getScreen()).onBackPress();
                return true;
            }
            return ((ScreenHandler) game.getScreen()).keyUp(keycode);
        }

        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            temp.set(screenX, screenY, 0);
            game.camera.unproject(temp);
            return ((ScreenHandler) game.getScreen()).touchUp(temp.x, temp.y);
        }

        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            temp.set(screenX, screenY, 0);
            game.camera.unproject(temp);
            return ((ScreenHandler) game.getScreen()).touchDown(temp.x, temp.y);
        }

        public boolean touchDragged(int screenX, int screenY, int pointer) {
            temp.set(screenX, screenY, 0);
            game.camera.unproject(temp);
            return ((ScreenHandler) game.getScreen()).touchDragged(temp.x, temp.y);
        }
    }

    class Gesture extends GestureDetector {
        public Gesture(final MyGame game) {
            super(new GestureAdapter() {
                public boolean fling(float velocityX, float velocityY, int button) {
                    return ((ScreenHandler) game.getScreen()).fling(velocityX, velocityY);
                }

                @Override
                public boolean tap(float x, float y, int count, int button) {
                    temp.set(x, y, 0);
                    game.camera.project(temp);
                    return ((ScreenHandler) game.getScreen()).tap(temp.x, temp.y, count);
                }
            });
        }

    }
}
