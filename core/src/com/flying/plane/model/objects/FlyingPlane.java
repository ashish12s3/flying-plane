package com.flying.plane.model.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlyingPlane extends Sprite {
    private final Animation<TextureRegion> animation;
    private final ParticleEffect particleEffect;
    private boolean isStop = true;
    private float stateTime;

    public FlyingPlane() {
        TextureRegion[] regions = new TextureRegion[3];
        for (int i = 0; i < regions.length; i++) {
            Texture texture = new Texture(Gdx.files.internal("planeGreen" + (i + 1) + ".png"));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            regions[i] = new TextureRegion(texture);
        }
        set(new Sprite(regions[0]));
        animation = new Animation<TextureRegion>(0.2f, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/smoke"), Gdx.files.internal("effects"));
        particleEffect.start();
    }

    @Override
    public void draw(Batch batch) {
//        super.draw(batch);
        if (!isStop)
            stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > 0) {
            particleEffect.setPosition(getX() + 20, getY() + getHeight() / 2);
            particleEffect.draw(batch,Gdx.graphics.getDeltaTime());
        }
        batch.draw(animation.getKeyFrame(isStop ? 0 : stateTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void setStop(boolean b) {
        isStop = b;
    }
}
