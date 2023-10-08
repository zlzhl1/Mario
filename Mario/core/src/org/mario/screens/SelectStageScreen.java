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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
public class SelectStageScreen implements Screen {

    final Game game;
    private OrthographicCamera camera;
    private Music music;
    private Stage stage;
    private TextButton stageO;
    private TextButton stage1;
    private TextButton stage2;
    private TextButton stage3;
    private TextButton back;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton.TextButtonStyle textButtonStyle;
    private float startX;
    private float startY;
    private float centerY;
    private float scale = 1.0f;
    private boolean isMusicToPlay = true;

    /**
     * Constructor
     *
     * @param game The obtained game object
     */
    public SelectStageScreen(final Game game) {
        this.game = game;
        music = AudioLoader.backgroundMusic;
        music.setLooping(true);

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = game.font;
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

        stageO = new TextButton("1-1", buttonStyle);
        stage1 = new TextButton("1-2", buttonStyle);
        stage2 = new TextButton("1-3", buttonStyle);
        stage3 = new TextButton("1-4", buttonStyle);
        back = new TextButton("BACK", buttonStyle);

        setUpListener();
        stage.addActor(stageO);
        stage.addActor(stage1);
        stage.addActor(stage2);
        stage.addActor(stage3);
        stage.addActor(back);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        if (Gdx.graphics.getWidth() != 1440 && Gdx.graphics.getHeight() != 720) {
            scale = Math.min((Gdx.graphics.getWidth() / 1280f), (Gdx.graphics.getHeight() / 720f));
            stageO.setTransform(true);
            stageO.setScale(scale);
            stage1.setTransform(true);
            stage1.setScale(scale);
            stage2.setTransform(true);
            stage2.setScale(scale);
            stage3.setTransform(true);
            stage3.setScale(scale);
            back.setTransform(true);
            back.setScale(scale);
        }

        startX = Gdx.graphics.getWidth() / 2f;
        startY = Gdx.graphics.getHeight() * 3 / 5f;
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

        stageO.setSize(Gdx.graphics.getWidth() / 5.2f, 100);
        stage1.setSize(Gdx.graphics.getWidth() / 5.2f, 100);
        stage2.setSize(Gdx.graphics.getWidth() / 5.2f, 100);
        stage3.setSize(Gdx.graphics.getWidth() / 5.2f, 100);
        back.setSize(Gdx.graphics.getWidth() / 4f, 100);

        float buttonWidth = stageO.getWidth() * scale;
        float buttonHigh = stageO.getHeight() * scale;
        float buttonSpacing = 20 * scale;

        stageO.setPosition(startX - buttonWidth - buttonSpacing / 2f, startY);
        stage1.setPosition(startX + buttonSpacing / 2f, startY);
        stage2.setPosition(
                startX - buttonWidth - buttonSpacing / 2f, startY - buttonHigh - buttonSpacing);
        stage3.setPosition(startX + buttonSpacing / 2f, startY - buttonHigh - buttonSpacing);
        back.setPosition(startX - buttonWidth - buttonSpacing / 2f, startY - buttonHigh - centerY);

        stage.draw();

        game.batch.end();
    }

    /** Listening function */
    private void setUpListener() {
        stageO.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new InGameScreen(game, "1-1"));
                        dispose();
                    }
                });
        stage1.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new InGameScreen(game, "1-2"));
                        dispose();
                    }
                });
        stage2.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(
                                new MessageScreen(
                                        game, "WAIT A MINUTE", "This level is under development"));
                        dispose();
                    }
                });
        stage3.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(
                                new MessageScreen(
                                        game, "WAIT A MINUTE", "This level is under development"));
                        dispose();
                    }
                });
        back.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new MenuScreen(game));
                        dispose();
                    }
                });
    }

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
