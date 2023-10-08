package org.mario.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */

/**
 * This class is used to display the HUD(Head-Up Display) in the game Include timer, score, level,
 * world, etc
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    // Constructs a method that initializes each tag
    public Hud(SpriteBatch spriteBatch, BitmapFont font) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top(); // Set the table top alignment
        table.setFillParent(true); // Set the table to fill the entire stage

        // Initializes each label
        countdownLabel =
                new Label(
                        String.format("%03d", worldTimer), new Label.LabelStyle(font, Color.WHITE));
        scoreLabel =
                new Label(String.format("%06d", score), new Label.LabelStyle(font, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(font, Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(font, Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(font, Color.WHITE));

        // Add the label to the table
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    // Update timer
    public void update(float deltaTime) {
        timeCount += deltaTime; // Update time counter
        if (timeCount >= 1) {
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer)); // Update countdown
            timeCount = 0;
        }
    }

    // Increase score
    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score)); // Update score
    }

    // Gets the value of the countdown
    public Integer getWorldTimer() {
        return worldTimer;
    }

    // Set level text
    public void setLevel(String level) {
        levelLabel.setText(level);
    }
}
