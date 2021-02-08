package com.flying.plane.screens;

import com.badlogic.gdx.Screen;

public interface ScreenHandler extends Screen {

	boolean onBackPress();

	boolean touchDown(float x, float y);

	boolean touchUp(float x, float y);

	boolean touchDragged(float x, float y);

	boolean fling(float x, float y);

	boolean keyUp(int keycode);

	boolean keyDown(int keycode);

	boolean tap(float x, float y, int count);

}
