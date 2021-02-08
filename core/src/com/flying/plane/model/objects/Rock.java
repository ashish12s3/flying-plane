package com.flying.plane.model.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Rock extends Sprite {
    RockPosition position;

    public Rock(GroundType rockType, RockPosition position) {
        updateImage(rockType,position);
    }

    private void updateImage(GroundType rockType, RockPosition position) {
        this.position=position;
        Texture texture = null;
        switch (rockType) {
            case GRASS:
                texture = new Texture(Gdx.files.internal(position==RockPosition.DOWN?"rockGrass.png":"rockGrassDown.png"));
                break;
            case ICE:
                texture = new Texture(Gdx.files.internal(position==RockPosition.DOWN?"rockIce.png":"rockIceDown.png"));
                break;
            case ROCK:
                texture = new Texture(Gdx.files.internal(position==RockPosition.DOWN?"rock.png":"rockDown.png"));
                break;
            default:
                texture = new Texture(Gdx.files.internal(position==RockPosition.DOWN?"rockSnow.png":"rockSnowDown.png"));
                break;
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        set(new Sprite(texture));
    }
}
