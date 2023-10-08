package org.mario.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.mario.Game;
import org.mario.hud.Hud;
import org.mario.listener.WorldContactListener;
import org.mario.sprites.enemy.Enemy;
import org.mario.sprites.items.Item;
import org.mario.sprites.items.ItemDef;
import org.mario.sprites.items.Mushroom;
import org.mario.sprites.player.Mario;
import org.mario.sprites.player.MarioState;
import org.mario.utils.AudioLoader;
import org.mario.utils.WorldCreator;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class InGameScreen implements Screen {
    private final Game game;
    private TextureAtlas atlas;
    private Hud hud;
    private OrthographicCamera
            gameCam; // This is a class for creating orthogonal projection matrices
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private WorldCreator worldCreator;
    private Mario player;
    private Music music;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private boolean isMusicToPlay = true;
    private String currentMap = "1-1";

    public InGameScreen(final Game game, String mapName) {
        // Initialize the properties
        this.game = game;

        this.currentMap = mapName;
        music = AudioLoader.backgroundMusic;
        music.setLooping(true);

        atlas = new TextureAtlas("sprites/sprites.pack"); // Get texture atlas file

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Game.V_WIDTH / Game.PPM, Game.V_HEIGHT / Game.PPM, gameCam);
        hud = new Hud(game.batch, game.font);
        hud.setLevel(currentMap);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/" + currentMap + ".tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Game.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);

        worldCreator = new WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingQueue<>();
    }

    /**
     * Used to spawn items
     *
     * @param itemDef ItemDef
     */
    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    /** Control the spawn items */
    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == Mushroom.class) {
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    /**
     * Judgment game over
     *
     * @return boolean
     */
    public boolean gameOver() {
        return (player.getCurrentState() == MarioState.DEAD && player.getStateTimer() > 3)
                || player.getBody().getPosition().y < -1;
    }

    /**
     * Judgment game win
     *
     * @return boolean
     */
    public boolean gameWin() {
        return player.getCurrentState() == MarioState.WIN && player.getStateTimer() > 5;
    }

    private boolean spaceKeyWasPressed = false;

    /**
     * This method updates the game world and its entities based on the elapsed time (delta).
     *
     * @param delta The time in seconds since the last render.
     */
    public void update(float delta) {

        if (player.getCurrentState() != MarioState.DEAD
                && isMusicToPlay) { // According to Mario to control the music switch
            music.play();
        } else {
            music.stop();
        }

        boolean spaceKeyPressed =
                Gdx.input.isKeyPressed(Input.Keys.W); // Prevent multiple trigger jumps

        handleInput(delta);
        handleSpawningItems();

        if (!spaceKeyPressed && spaceKeyWasPressed) {
            player.setIsJumping(false);
        }
        spaceKeyWasPressed = spaceKeyPressed;

        world.step(1 / 60f, 6, 2);

        player.update(delta);

        for (Enemy enemy : worldCreator.getEnemies()) { // update enemies
            enemy.update(delta);
            if (enemy.getX() < player.getX() + 224 / Game.PPM) {
                enemy.getBody().setActive(true);
            }
        }

        for (Item item : items) {
            item.update(delta);
        }

        hud.update(delta);

        if (player.getCurrentState() != MarioState.DEAD) {
            if (player.getBody().getPosition().x < 2) gameCam.position.x = 2;
            else if (player.getBody().getPosition().x >= 2
                    && player.getBody().getPosition().x
                            < (float) (map.getProperties().get("width", Integer.class)) / 6.6)
                gameCam.position.x = player.getBody().getPosition().x;
            else
                gameCam.position.x =
                        (float) ((float) (map.getProperties().get("width", Integer.class)) / 6.6);
        }

        gameCam.update();
        renderer.setView(gameCam);

        if (gameOver() || hud.getWorldTimer() == 0) {
            game.setScreen(new MessageScreen(game, "GAME OVER", "Press any key to play again"));
        }

        if (gameWin()) {
            game.setScreen(new MessageScreen(game, "YOU WIN", "Press any key to play again"));
        }
    }

    /**
     * This function sets the initial position of the interface and the character in the game, and
     * specifies that the character cannot go beyond the interface.
     *
     * @param delta The time in seconds since the last render.
     */
    public void handleInput(float delta) {
        float mapWidth =
                map.getProperties().get("width", Integer.class)
                        * map.getProperties().get("tilewidth", Integer.class)
                        / Game.PPM;
        float mapHeight =
                map.getProperties().get("height", Integer.class)
                        * map.getProperties().get("tileheight", Integer.class)
                        / Game.PPM;
        float playerWidth = player.getWidth() / Game.PPM;
        float playerHeight = player.getHeight() / Game.PPM;

        if (player.getCurrentState() != MarioState.DEAD) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (player.getCurrentState() != MarioState.JUMPING
                        && player.getCurrentState() != MarioState.GROWING
                        && !player.getIsJumping()) {
                    player.getBody()
                            .applyLinearImpulse(
                                    new Vector2(0, 4.0f), player.getBody().getWorldCenter(), true);
                    player.setIsJumping(true);
                }
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                    && player.getBody().getLinearVelocity().x <= 2)
                player.getBody()
                        .applyLinearImpulse(
                                new Vector2(0.1f, 0), player.getBody().getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                    && player.getBody().getLinearVelocity().x >= -2)
                player.getBody()
                        .applyLinearImpulse(
                                new Vector2(-0.1f, 0), player.getBody().getWorldCenter(), true);
        }

        // when enter esc return to menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            // stop music
            music.stop();
            game.setScreen(new MenuScreen(game));
        }

        if (player.getBody().getPosition().x - playerWidth < 0) {
            player.getBody().setLinearVelocity(2, player.getBody().getLinearVelocity().y);
            player.getBody().setTransform(0, player.getBody().getPosition().y, 0);
        } else if (player.getBody().getPosition().x + playerWidth > mapWidth) {
            player.getBody().setLinearVelocity(-2, player.getBody().getLinearVelocity().y);
            player.getBody()
                    .setTransform(
                            mapWidth - playerWidth - 0.08F, player.getBody().getPosition().y, 0);
        }
    }

    /** Called when this screen becomes the current screen for a {@link com.badlogic.gdx.Game}. */
    @Override
    public void show() {}

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1f); // set background color
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT); // clear screen

        renderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        if (player.getCurrentState() == MarioState.CLIMBING) {
            game.batch.draw(
                    player.getFrame(delta),
                    player.getX(),
                    player.getY(),
                    player.getWidth(),
                    player.getHeight());
        }

        for (Enemy enemy : worldCreator.getEnemies()) enemy.draw(game.batch);

        for (Item item : items) item.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getBatch().getProjectionMatrix());
        hud.stage.draw();
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
        gamePort.update(width, height);
    }

    /**
     * @see ApplicationListener#pause()
     */
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

    /** Called when this screen should release all resources. */
    @Override
    public void dispose() {
        music.stop();
        map.dispose();
        renderer.dispose();
        world.dispose();
        hud.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public String getCurrentMap() {
        return currentMap;
    }
}
