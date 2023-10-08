package org.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import org.mario.Game;
import org.mario.utils.AudioLoader;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class SettingScreen implements Screen {
    final Game game;
    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;
    private TextButton soundVolume;
    private float soundCount = 0.5f;
    private TextButton fullScreen;
    private TextButton back;
    private TextButton.TextButtonStyle buttonStyle;
    private Music music;
    private float centerX;
    private float centerY;
    private float scale = 1.0f;
    private boolean isMusicToPlay = true;

    /**
     * Constructor
     *
     * @param game The obtained game object
     */
    public SettingScreen(final Game game) {
        this.game = game;

        music = AudioLoader.backgroundMusic;
        music.setLooping(true);

        stage = new Stage();

        // This call should be after initialisation of stage.
        Gdx.input.setInputProcessor(stage);

        NinePatch buttonPatch =
                new NinePatch(
                        new Texture(Gdx.files.internal("images/button/button2.png.9.png")),
                        0,
                        0,
                        0,
                        0);
        TextureRegion buttonRegion = new TextureRegion(buttonPatch.getTexture());
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(buttonRegion);
        textureRegionDrawable.setPadding(0, 0, 0, 0);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new NinePatchDrawable(buttonPatch);
        buttonStyle.down = null;
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.valueOf("#FB9966");

        if (music.getVolume() != 0) {
            soundVolume = new TextButton("MUTE", buttonStyle);
        } else {
            soundVolume = new TextButton("UNMUTE", buttonStyle);
        }
        fullScreen = new TextButton("Full Screen", buttonStyle);
        back = new TextButton("Back", buttonStyle);

        setUpListener();
        stage.addActor(soundVolume);
        stage.addActor(fullScreen);
        stage.addActor(back);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        if (Gdx.graphics.getWidth() != 1440 && Gdx.graphics.getHeight() != 720) {
            scale = Math.min((Gdx.graphics.getWidth() / 1280f), (Gdx.graphics.getHeight() / 720f));
            soundVolume.setTransform(true);
            soundVolume.setScale(scale);
            fullScreen.setTransform(true);
            fullScreen.setScale(scale);
            back.setTransform(true);
            back.setScale(scale);
        }

        background = new Texture(Gdx.files.internal("images/setting/background.jpg"));

        centerX = Gdx.graphics.getWidth() / 2f;
        centerY = Gdx.graphics.getHeight() / 3f;
    }

    @Override
    public void show() {}

    /**
     * Put multiple buttons into the screen according to a certain rule, and can adapt to the size
     * of the window
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        stage.act(delta);

        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        if (isMusicToPlay) music.play();
        else music.pause();

        game.batch.draw(background, 0, 0);

        soundVolume.setPosition(
                centerX - soundVolume.getWidth() * scale / 2, centerY * 2f - (70f) * scale);
        fullScreen.setPosition(
                centerX - fullScreen.getWidth() * scale / 2, centerY * 2f - (140f) * scale);
        back.setPosition(centerX - back.getWidth() * scale / 2, centerY * 2f - (210f) * scale);

        game.batch.end();

        stage.draw();
    }

    /** Listening function */
    private void setUpListener() {
        soundVolume.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (music.getVolume() != 0) {
                            music.setVolume(0);
                            soundVolume.setText("UNMUTE");
                        } else {
                            music.setVolume(1);
                            soundVolume.setText("MUTE");
                        }
                    }
                });
        fullScreen.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (Gdx.graphics.isFullscreen()) {
                            Gdx.graphics.setWindowedMode(1440, 720);
                        } else {
                            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                        }
                    }
                });
        back.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new MenuScreen(game));
                    }
                });
    }

    public void winListener() {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        isMusicToPlay = false;
    }

    @Override
    public void resume() {
        isMusicToPlay = true;
    }

    @Override
    public void hide() {
        isMusicToPlay = false;
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.stop();
    }
}
