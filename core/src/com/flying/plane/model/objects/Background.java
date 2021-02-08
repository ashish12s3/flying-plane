package com.flying.plane.model.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.flying.plane.MyGame;

public class Background extends Rectangle {
    private final Texture texture;
    private final float ratio;
    private float diff = 0;
    private float speed = 0f;

    public Background() {
        setSize(MyGame.WIDTH, MyGame.HEIGHT);
        texture = new Texture(Gdx.files.internal("background.png"));
        ratio = texture.getWidth() / MyGame.WIDTH;
    }

    public void draw(Batch batch) {
        batch.draw(texture, 0, 0, MyGame.WIDTH - diff, MyGame.HEIGHT, (int) (diff * ratio), 0, (int) (texture.getWidth() - (diff * ratio)), (int) texture.getHeight(), false, false);
        batch.draw(texture, MyGame.WIDTH - diff, 0, diff, MyGame.HEIGHT, 0, 0, (int) (diff * ratio), (int) texture.getHeight(), false, false);

        diff = (diff + speed) % MyGame.WIDTH;

    }

    public void setSpeed(float v) {
        speed = v;
    }
}
