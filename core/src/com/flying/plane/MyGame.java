package com.flying.plane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.flying.plane.dialogs.MyDialog;
import com.flying.plane.screens.GameScreen;
import com.flying.plane.util.Toast;

public class MyGame extends Game {
    public static final float WIDTH = 1280;
    public static final float HEIGHT = 720;
    public SpriteBatch batch;
    public Skin skin;
    public Stage stage;
    public OrthographicCamera camera;
    public AssetManager manager = new AssetManager();
    //	public Preferences pref;
    private Color bgColor = Color.valueOf("ffffff");

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        camera.update();

        stage = new Stage(new ScalingViewport(Scaling.stretch, WIDTH, HEIGHT, camera));
        skin = new Skin();
        MyDialog.addPopupBgToSkin(skin);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputHandler(this));
        Gdx.input.setInputProcessor(multiplexer);

        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.render();
        batch.end();
        stage.act();
        stage.draw();

        batch.begin();
        Toast.update(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        stage.dispose();
        skin.dispose();
    }

    public Drawable getPopupTransparentBg() {
        return skin.getDrawable("popupBg");
    }
}
