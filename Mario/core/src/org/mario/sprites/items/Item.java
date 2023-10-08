package org.mario.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
public abstract class Item extends Sprite {
    protected InGameScreen screen;
    protected Body body;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean isDestroyed;

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Item(InGameScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / Game.PPM, 16 / Game.PPM);
        defineItem();
        toDestroy = false;
        isDestroyed = false;
    }

    public abstract void defineItem();

    public abstract void use(Mario mario);

    /**
     * This method updates enemies based on the elapsed time (delta).
     *
     * @param dt The time in seconds since the last render.
     */
    public void update(float dt) {
        if (toDestroy && !isDestroyed) {
            world.destroyBody(body);
            isDestroyed = true;
        }
    }

    /**
     * Draw enemies
     *
     * @param batch The obtained Batch object
     */
    public void draw(Batch batch) {
        if (!isDestroyed) {
            super.draw(batch);
        }
    }

    /** Sets whether to be destroyed */
    public void destroy() {
        toDestroy = true;
    }

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
}
