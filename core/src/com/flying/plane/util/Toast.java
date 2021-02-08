package com.flying.plane.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.flying.plane.MyGame;

public class Toast {
    private static Array<Toast> toasts = new Array<Toast>();
    private static BitmapFont font = new BitmapFont();
    private String msg;
    private long duration;
    private long startTime;
    private boolean isAvailable;
    private Vector2 pos;
    public static final long LENGTH_LONG = 3000, LENGTH_SHORT = 2000;
    private static Texture bg;
    private static int TARGET_HEIGHT = (int) MyGame.HEIGHT;
    private static int TARGET_WIDTH = (int) MyGame.WIDTH;
    private static GlyphLayout layout = new GlyphLayout();

    static {
        Pixmap pixmap = createRoundedRectangle(128,32,10,new Color(0,0,0,0.7f));
        bg = new Texture(pixmap);
        bg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    public static Toast makeText(String msg, long duration) {
        Toast toast = new Toast(msg, duration);
        toasts.add(toast);
        return toast;
    }

    public static void setTargetWidhtHeight(float targetWidth, float targetHeight) {
        TARGET_WIDTH = (int) targetWidth;
        TARGET_HEIGHT = (int) targetHeight;
    }

    public static Toast makeText(String msg, long duration, Vector2 position) {
        Toast toast = new Toast(msg, duration);
        toast.pos = position;
        toasts.add(toast);
        return toast;
    }

    private Toast(String msg, long duration) {
        this.msg = msg;
        this.duration = duration;
        pos = new Vector2(TARGET_WIDTH / 2, TARGET_HEIGHT / 4);
    }

    public void show() {
        isAvailable = true;
        startTime = System.currentTimeMillis();
    }

    public static void update(Batch batch) {
        if (toasts.size == 0)
            return;
        // for (int i = 0; i < toasts.size; i++) {
        Toast toast = toasts.get(0);
        toast.draw(batch);
        if (!toast.isAvailable)
            toasts.removeIndex(0);
        // }
    }

    private void draw(Batch batch) {
        if (isAvailable && System.currentTimeMillis() - startTime <= duration) {
            float width = getWidth(font, msg);
            batch.draw(bg, pos.x - width / 2 - 20, pos.y - (1.3f * font.getLineHeight()), width + 40,
                    (2f * font.getLineHeight()));
            font.draw(batch, msg, pos.x - width / 2, pos.y);
        } else {
            isAvailable = false;
        }
    }

    private float getWidth(BitmapFont font, String text) {
        layout.setText(font, text);
        return layout.width;
    }

    public static void setDefaultFont(BitmapFont defFont) {
        font = defFont;
    }

    public void setPosition(int x, int y) {
        pos.set(x, y);

    }

    public static Pixmap createRoundedRectangle(int width, int height, int cornerRadius, Color color) {

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap ret = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(color);

        pixmap.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, cornerRadius, cornerRadius);
        pixmap.fillCircle(cornerRadius, height - cornerRadius - 1, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, height - cornerRadius - 1, cornerRadius);

        pixmap.fillRectangle(cornerRadius, 0, width - cornerRadius * 2, height);
        pixmap.fillRectangle(0, cornerRadius, width, height - cornerRadius * 2);

        ret.setColor(color);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (pixmap.getPixel(x, y) != 0) ret.drawPixel(x, y);
            }
        }
        pixmap.dispose();

        return ret;
    }
}
