package org.mario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.mario.screens.MenuScreen;
import org.mario.utils.AudioLoader;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class Game extends com.badlogic.gdx.Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 200;
    public static final float PPM = 100;

    @Override
    public void create() {
        new AudioLoader();
        batch = new SpriteBatch();
        // set up font
        font = new BitmapFont(Gdx.files.internal("font/mario-font.fnt"));
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
