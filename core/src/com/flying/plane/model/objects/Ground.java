package com.flying.plane.model.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.flying.plane.MyGame;

public class Ground extends Sprite {
    private GroundType type;
    Rock rock;

    public Ground(GroundType type) {
        updateImage(type);
    }

    public void updateImage(GroundType type) {
        this.type = type;
        Texture texture = null;
        switch (type) {

            case DIRT:

                texture = new Texture(Gdx.files.internal("groundDirt.png"));
                break;
            case GRASS:
                texture = new Texture(Gdx.files.internal("groundGrass.png"));
                break;
            case ICE:
                texture = new Texture(Gdx.files.internal("groundIce.png"));
                break;
            case ROCK:
                texture = new Texture(Gdx.files.internal("groundRock.png"));
                break;
            default:
                texture = new Texture(Gdx.files.internal("groundSnow.png"));
                break;
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        set(new Sprite(texture));

        if (type != GroundType.DIRT) {
            rock = new Rock(type, MathUtils.randomBoolean() ? RockPosition.UP : RockPosition.DOWN);
            rock.setY(rock.position == RockPosition.UP ? MyGame.HEIGHT - rock.getHeight() : 0);
            setPosition(getX(), getY());
        } else
            rock = null;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (rock != null)
            rock.draw(batch);

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (rock != null)
            rock.setX(getX() + (getWidth() - rock.getWidth()) / 2);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        if (rock != null)
            rock.setX(getX() + (getWidth() - rock.getWidth()) / 2);
    }

    public boolean overlap(Rectangle rectangle) {
        return getBoundingRectangle().overlaps(rectangle) || (rock != null && rock.getBoundingRectangle().overlaps(rectangle));
    }
}

