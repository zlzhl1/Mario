package org.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.mario.Game;
import org.mario.utils.AudioLoader;

import java.util.Random;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class MenuScreen implements Screen {
    final Game game;
    private OrthographicCamera camera;
    private Texture logo;
    private Texture background;
    private Texture CLOUD1;
    private Texture CLOUD2;
    private Texture CLOUD3;
    private Texture CLOUD4;
    private Stage stage;
    private TextButton play;
    private TextButton instructions;
    private TextButton exit;
    private TextButton settings;
    private TextButton.TextButtonStyle buttonStyle;
    private Music music;
    private float centerX;
    private float centerY;
    private float scale = 1.0f;
    private boolean isMusicToPlay = true;
    float cloud1Offset = 600 + randomInRange(-40, 30);
    float cloud2Offset = 600 + randomInRange(-40, 30);
    float cloud3Offset = 600 + randomInRange(-40, 30);
    float cloud4Offset = 600 + randomInRange(-40, 30);

    /**
     * This is the constructor for MenuScreen
     *
     * @param game Obtained game object
     */
    public MenuScreen(final Game game) {
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

        play = new TextButton("Play", buttonStyle);
        instructions = new TextButton("Instructions", buttonStyle);
        settings = new TextButton("Settings", buttonStyle);
        exit = new TextButton("Exit", buttonStyle);

        setUpListener();
        stage.addActor(play);
        stage.addActor(instructions);
        stage.addActor(settings);
        stage.addActor(exit);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        logo = new Texture(Gdx.files.internal("images/start/start-logo.png"));

        if (Gdx.graphics.getWidth() != 1440 && Gdx.graphics.getHeight() != 720) {
            scale = Math.min((Gdx.graphics.getWidth() / 1280f), (Gdx.graphics.getHeight() / 720f));
            play.setTransform(true);
            play.setScale(scale);
            instructions.setTransform(true);
            instructions.setScale(scale);
            settings.setTransform(true);
            settings.setScale(scale);
            exit.setTransform(true);
            exit.setScale(scale);
        }

        CLOUD1 = new Texture(Gdx.files.internal("images/start/cloud1.png"));
        CLOUD2 = new Texture(Gdx.files.internal("images/start/cloud2.png"));
        CLOUD3 = new Texture(Gdx.files.internal("images/start/cloud3.png"));
        CLOUD4 = new Texture(Gdx.files.internal("images/start/cloud4.png"));

        background = new Texture(Gdx.files.internal("images/start/menu-background.png"));

        centerX = Gdx.graphics.getWidth() / 2f;
        centerY = Gdx.graphics.getHeight() / 3f;
    }

    @Override
    public void show() {}

    /**
     * Add components and display
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

        game.batch.draw(
                logo,
                centerX - logo.getWidth() * scale / 2f,
                centerY * 2f - logo.getHeight() * scale / 2f,
                logo.getWidth() * scale,
                logo.getHeight() * scale);

        play.setPosition(
                centerX - play.getWidth() * scale / 2,
                centerY * 2f - (logo.getHeight() / 2f + 70f) * scale);
        instructions.setPosition(
                centerX - instructions.getWidth() * scale / 2,
                centerY * 2f - (logo.getHeight() / 2f + 140f) * scale);
        settings.setPosition(
                centerX - settings.getWidth() * scale / 2,
                centerY * 2f - (logo.getHeight() / 2f + 210f) * scale);
        exit.setPosition(
                centerX - exit.getWidth() * scale / 2,
                centerY * 2f - (logo.getHeight() / 2f + 280f) * scale);

        stage.draw();

        renderScrollingStuff(game.batch);

        game.batch.end();
    }

    /**
     * Generate cloud and implement mobile logic
     *
     * @param batch Obtained batch object
     */
    private void renderScrollingStuff(SpriteBatch batch) {

        float time = TimeUtils.nanoTime() / 100000000.0f;

        batch.draw(
                CLOUD1,
                (float)
                        (-CLOUD1.getWidth()
                                + time * 0.5 % (CLOUD1.getWidth() + Gdx.graphics.getWidth())),
                cloud1Offset,
                CLOUD1.getWidth() * 3 * scale,
                CLOUD1.getHeight() * 3 * scale);
        batch.draw(
                CLOUD2,
                (float)
                        (-CLOUD2.getWidth()
                                + time * 0.6 % (CLOUD2.getWidth() + Gdx.graphics.getWidth())),
                cloud2Offset,
                CLOUD2.getWidth() * 3 * scale,
                CLOUD2.getHeight() * 3 * scale);
        batch.draw(
                CLOUD3,
                (float)
                        (-CLOUD3.getWidth()
                                + time * 0.7 % (CLOUD3.getWidth() + Gdx.graphics.getWidth())),
                cloud3Offset,
                CLOUD3.getWidth() * 3 * scale,
                CLOUD3.getHeight() * 3 * scale);
        batch.draw(
                CLOUD4,
                (float)
                        (-CLOUD4.getWidth()
                                + time * 0.8 % (CLOUD4.getWidth() + Gdx.graphics.getWidth())),
                cloud4Offset,
                CLOUD4.getWidth() * 3 * scale,
                CLOUD4.getHeight() * 3 * scale);
    }

    /**
     * Generate random numbers
     *
     * @param min Lower bound of random numbers
     * @param max Upper limit of random numbers
     * @return Final results
     */
    public static int randomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /** Add listeners for the interface */
    private void setUpListener() {
        play.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new SelectStageScreen(game));
                        dispose();
                    }
                });
        instructions.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new InstructionScreen(game));
                        dispose();
                    }
                });
        settings.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new SettingScreen(game));
                        dispose();
                    }
                });
        exit.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.exit(0);
                    }
                });
    }

    /**
     * Maintain normal interface layout
     *
     * @param width The width of the interface
     * @param height The height of the interface
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /** Stop music playback under pause conditions */
    @Override
    public void pause() {
        isMusicToPlay = false;
    }

    /** Stop music playback under resume conditions */
    @Override
    public void resume() {
        isMusicToPlay = true;
    }

    /** Stop music playback under hide conditions */
    @Override
    public void hide() {
        isMusicToPlay = false;
    }

    /** Handling of closing the interface */
    @Override
    public void dispose() {
        stage.dispose();
        music.stop();
    }
}
