package org.mario.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected InGameScreen screen;

    protected Body body;

    protected Vector2 velocity;

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Enemy(InGameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        createEnemy();
        velocity = new Vector2(0.5f, 0);
        body.setActive(false);
        body.setGravityScale(5);
    }

    protected abstract void createEnemy();

    public abstract void onHit(Mario mario);

    public abstract void update(float delta);

    /**
     * Change the direction of the velocity
     *
     * @param x Choose the x or y direction
     * @param y Choose the x or y direction
     */
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

    /**
     * Get the body
     *
     * @return body
     */
    public Body getBody() {
        return body;
    }

    public abstract void onEnemyHit(Enemy userData);

    /**
     * Set the speed of different enemies
     *
     * @param fixtureDef The obtained fixtureDef object
     */
    protected static void defFixtureDef(FixtureDef fixtureDef) {
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / Game.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / Game.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / Game.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / Game.PPM);
        head.set(vertices);
        fixtureDef.shape = head;
    }
}
