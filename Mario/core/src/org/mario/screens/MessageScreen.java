package org.mario.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.mario.Game;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class MessageScreen implements Screen {
    private Game game;
    private Viewport viewport;
    private Stage stage;

    /**
     * Constructor
     *
     * @param game The obtained game object
     * @param Label1 The obtained String object
     * @param Label2 The obtained String object
     */
    public MessageScreen(Game game, String Label1, String Label2) {
        this.game = game;
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        Label.LabelStyle font = new Label.LabelStyle(game.font, Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label(Label1, font);
        Label playAgainLabel = new Label(Label2, font);
        // the text should be a little bit smaller
        gameOverLabel.setFontScale(0.2f);
        playAgainLabel.setFontScale(0.3f);
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);
        stage.addActor(table);
    }

    @Override
    public void show() {}

    public void update(float delta) {}

    /**
     * This method is called every frame to render the screen
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        update(delta);
    }

    /**
     * The component size is updated when the window size changes
     *
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
