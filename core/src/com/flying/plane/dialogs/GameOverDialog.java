package com.flying.plane.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.flying.plane.MyGame;

public class GameOverDialog extends MyDialog {
    public GameOverDialog(MyGame game) {
        super(game);
        Texture texture = new Texture(Gdx.files.internal("textGameOver.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image bg = new Image(texture);
        bg.setPosition(MyGame.WIDTH / 2, MyGame.HEIGHT / 2, Align.center);
        addActor(bg);
        bg.setOrigin(Align.center);
        bg.addAction(Actions.sequence(Actions.scaleTo(0, 0), Actions.scaleTo(1, 1, 0.5f)));
    }
}
