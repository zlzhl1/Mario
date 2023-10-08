package org.mario.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import org.mario.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.mario.utils.AudioLoader;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class InstructionScreen implements Screen {
    final Game game;
    private Music music;
    private Stage stage;
    private Texture pic1;
    private Texture pic2;
    private Texture pic3;
    private Texture pic4;
    private Table table;
    private TextButton.TextButtonStyle buttonStyle;
    private boolean isMusicToPlay = true;

    /**
     * This is the constructor for InstructionScreen
     *
     * @param game Obtained game object
     */
    public InstructionScreen(final Game game) {
        this.game = game;
        music = AudioLoader.backgroundMusic;
        music.setLooping(true);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.pad(20).defaults().space(30);
        pic1 = new Texture(Gdx.files.internal("images/instruction/operate1.png"));
        pic2 = new Texture(Gdx.files.internal("images/instruction/operate2.png"));
        pic3 = new Texture(Gdx.files.internal("images/instruction/operate3.png"));
        pic4 = new Texture(Gdx.files.internal("images/instruction/operate4.png"));
        Image image1 = new Image(pic1);
        Image image2 = new Image(pic2);
        Image image3 = new Image(pic3);
        Image image4 = new Image(pic4);
        table.add(image1).size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.row();
        table.add(image2).size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.row();
        table.add(image3).size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.row();
        table.add(image4).size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScrollPane instructionsScrollPane = new ScrollPane(table);
        instructionsScrollPane.setScrollingDisabled(true, false);
        instructionsScrollPane.setFadeScrollBars(false);
        instructionsScrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(instructionsScrollPane);
        stage.setScrollFocus(instructionsScrollPane);
    }

    /**
     * Create a return button suspended at the bottom
     *
     * @return Processed table object
     */
    private Table createBottomTable() {
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
        Table table = new Table();
        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new MenuScreen(game));
                    }
                });
        table.row().expand().bottom();
        table.add(new Actor()).expandY();
        table.row().center();
        table.add(backButton).padBottom(20);
        table.setFillParent(true);
        return table;
    }

    /** Add the return button to the screen */
    @Override
    public void show() {
        stage.addActor(createBottomTable());
    }

    /**
     * Add components and display
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (!isMusicToPlay) music.stop();
        else music.play();
        stage.act(delta);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.draw();
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
        music.stop();
    }
}
