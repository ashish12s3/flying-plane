package com.flying.plane.dialogs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;
import com.flying.plane.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Displays a dialog, which is a modal window containing a content table with a
 * button table underneath it. Methods are provided to add a label to the
 * content table and buttons to the button table, but any widgets can be added.
 * When a button is clicked, {@link #result(Object)} is called and the dialog is
 * removed from the stage.
 *
 * @author Nathan Sweet
 */
public class MyDialog extends MyWindow {
    Table contentTable, buttonTable;
    private Skin skin;
    ObjectMap<Actor, Object> values = new ObjectMap();
    boolean cancelHide;
    Actor previousKeyboardFocus, previousScrollFocus;
    FocusListener focusListener;


    protected InputListener ignoreTouchDown = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            event.cancel();
            return false;
        }
    };
    private boolean isFadingEnable;

    public MyDialog(String title, Skin skin) {
        super(title, skin.get(WindowStyle.class));
        this.skin = skin;
        initialize();
    }

    public MyDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin.get(windowStyleName, WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
    }

    public MyDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        initialize();
    }

//	public MyDialog(BitmapFont font, Color fontColor) {
//		super("", new WindowStyle(font, fontColor, ));
//		setWidth(800);
//		setHeight(480);
//		initialize();
//	}
//
//	public MyDialog(BitmapFont font) {
//		super("", new WindowStyle(font, Color.BLACK, skin2.getDrawable("bg")));
//		setWidth(800);
//		setHeight(480);
//		initialize();
//	}

    public MyDialog(MyGame game) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, game.getPopupTransparentBg()));
        setWidth(800);
        setHeight(480);
        initialize();
    }

    private void initialize() {
        setSize(MyGame.WIDTH, MyGame.HEIGHT);
        setModal(true);
        defaults().space(6);
        add(contentTable = new Table(skin)).expand().fill();
        row();
        add(buttonTable = new Table(skin));

        contentTable.defaults().space(6);
        buttonTable.defaults().space(6);

        buttonTable.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!values.containsKey(actor))
                    return;
                while (actor.getParent() != buttonTable)
                    actor = actor.getParent();
                result(values.get(actor));
                if (!cancelHide)
                    hide();
                cancelHide = false;
            }
        });

        focusListener = new FocusListener() {
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (!focused)
                    focusChanged(event);
            }

            public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (!focused)
                    focusChanged(event);
            }

            private void focusChanged(FocusEvent event) {
                Stage stage = getStage();
                if (isModal() && stage != null && stage.getRoot().getChildren().size > 0 && stage.getRoot().getChildren().peek() == MyDialog.this) { // Dialog
                    // is
                    // top
                    // most
                    // actor.
                    Actor newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(MyDialog.this) && !(newFocusedActor.equals(previousKeyboardFocus) || newFocusedActor.equals(previousScrollFocus)))
                        event.cancel();
                }
            }
        };

        addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                    onBackPressed();
                    return true;
                }
                return super.keyUp(event, keycode);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (anchorActor != null && ((x < anchorActor.getX() || x > anchorActor.getRight()) || (y < anchorActor.getY() || y > anchorActor.getTop()))) {
                    onBackPressed();
                }

            }
        });

    }

    private Actor anchorActor;

    public void setAnchorActor(Actor anchorActor) {
        this.anchorActor = anchorActor;
    }

    protected void onBackPressed() {
    }

    protected void setStage(Stage stage) {
        if (stage == null)
            addListener(focusListener);
        else
            removeListener(focusListener);
        super.setStage(stage);
    }

    public Table getContentTable() {
        return contentTable;
    }

    public Table getButtonTable() {
        return buttonTable;
    }

    /**
     * Adds a label to the content table. The dialog must have been constructed
     * with a skin to use this method.
     */
    public MyDialog text(String text) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        return text(text, skin.get(LabelStyle.class));
    }

    /**
     * Adds a label to the content table.
     */
    public MyDialog text(String text, LabelStyle labelStyle) {
        return text(new Label(text, labelStyle));
    }

    /**
     * Adds the given Label to the content table
     */
    public MyDialog text(Label label) {
        contentTable.add(label);
        return this;
    }

    /**
     * Adds a text button to the button table. Null will be passed to
     * {@link #result(Object)} if this button is clicked. The dialog must have
     * been constructed with a skin to use this method.
     */
    public MyDialog button(String text) {
        return button(text, null);
    }

    /**
     * Adds a text button to the button table. The dialog must have been
     * constructed with a skin to use this method.
     *
     * @param object The object that will be passed to {@link #result(Object)} if
     *               this button is clicked. May be null.
     */
    public MyDialog button(String text, Object object) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        return button(text, object, skin.get(TextButtonStyle.class));
    }

    /**
     * Adds a text button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)} if
     *               this button is clicked. May be null.
     */
    public MyDialog button(String text, Object object, TextButtonStyle buttonStyle) {
        return button(new TextButton(text, buttonStyle), object);
    }

    /**
     * Adds the given button to the button table.
     */
    public MyDialog button(Button button) {
        return button(button, null);
    }

    /**
     * Adds the given button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)} if
     *               this button is clicked. May be null.
     */
    public MyDialog button(Button button, Object object) {
        buttonTable.add(button);
        setObject(button, object);
        return this;
    }

    /**
     * {@link #pack() Packs} the dialog and adds it to the stage with custom
     * action which can be null for instant show
     */
    public MyDialog show(Stage stage, Action action) {
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
//                SoundManager.playPopUpSound();
            }
        },0f);

        clearActions();
        keepWithinStage = false;
        removeCaptureListener(ignoreTouchDown);

        previousKeyboardFocus = null;
        Actor actor = stage.getKeyboardFocus();
        if (actor != null && !actor.isDescendantOf(this))
            previousKeyboardFocus = actor;

        previousScrollFocus = null;
        actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this))
            previousScrollFocus = actor;

        pack();
        stage.addActor(this);
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        if (action != null)
            addAction(action);

        return this;
    }

    /**
     * {@link #pack() Packs} the dialog and adds it to the stage, centered with
     * default fadeIn action
     */
    public MyDialog show(Stage stage) {
        Action action = (Action) null;
        // Action action=sequence(Actions.moveTo(-720, 0),Actions.moveTo(0, 0,
        // 0.3f));
        if (isFadingEnable) {
            show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
        } else {
            table.setOrigin(Align.center);
            table.addAction(sequence(Actions.scaleTo(0, 0), Actions.scaleTo(1, 1, 0.4f, Interpolation.Elastic.swingOut)));
            show(stage, null);
        }

//        show(stage, isFadingEnable ? sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)) : action);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    public void setFadingAnimationEnable(boolean v) {
        isFadingEnable = v;
    }

    /**
     * Hides the dialog with the given action and then removes it from the
     * stage.
     */
    public void hide(Action action) {
        Stage stage = getStage();
        if (stage != null) {
            removeListener(focusListener);
            if (previousKeyboardFocus != null && previousKeyboardFocus.getStage() == null)
                previousKeyboardFocus = null;
            Actor actor = stage.getKeyboardFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setKeyboardFocus(previousKeyboardFocus);

            if (previousScrollFocus != null && previousScrollFocus.getStage() == null)
                previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setScrollFocus(previousScrollFocus);
        }
        if (action != null) {
            addCaptureListener(ignoreTouchDown);
            addAction(sequence(action, Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
        } else
            remove();
    }

    /**
     * Hides the dialog. Called automatically when a button is clicked. The
     * default implementation fades out the dialog over 400 milliseconds and
     * then removes it from the stage.
     */
    public void hide() {
        if (isFadingEnable)
            hide(sequence(fadeOut(0.4f, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
        else {
            // hide(sequence(Actions.moveTo(720, 0, 0.3f)));
            // hide(sequence(Actions.moveTo(-720, 0),Actions.moveTo(0, 0,
            // 0.5f)));
//            hide((Action) null);
            table.addAction(sequence(Actions.scaleTo(0, 0, 0.4f, Interpolation.Elastic.swingIn), Actions.run(new Runnable() {
                @Override
                public void run() {
                    hide((Action) null);
                }
            })));

        }

    }

    public void setObject(Actor actor, Object object) {
        values.put(actor, object);
    }

    /**
     * If this key is pressed, {@link #result(Object)} is called with the
     * specified object.
     *
     * @see Keys
     */
    public MyDialog key(final int keycode, final Object object) {
        addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    result(object);
                    if (!cancelHide)
                        hide();
                    cancelHide = false;
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Called when a button is clicked. The dialog will be hidden after this
     * method returns unless {@link #cancel()} is called.
     *
     * @param object The object specified when the button was added.
     */
    protected void result(Object object) {
    }

    public void cancel() {
        cancelHide = true;
    }

    public static void addPopupBgToSkin(Skin skin) {
        Pixmap labelColor = new Pixmap((int) MyGame.WIDTH, (int) MyGame.HEIGHT, Pixmap.Format.Alpha);
        Color black = Color.BLACK;
        labelColor.setBlending(Pixmap.Blending.SourceOver);
        black.a = 0.3f;
        labelColor.setColor(black);
        labelColor.fill();
        Texture texture = new Texture(labelColor);
        labelColor.dispose();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("popupBg", texture);
    }
}
