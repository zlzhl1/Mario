package org.mario.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;
import org.mario.utils.AudioLoader;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private boolean isDestroyed;
    private boolean isSetToDestroy;

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Goomba(InGameScreen screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(
                    new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation<>(0.4f, frames);
        stateTime = 0;

        isDestroyed = false;
        isSetToDestroy = false;

        setBounds(getX(), getY(), 16 / Game.PPM, 16 / Game.PPM);
    }

    /**
     * This method updates enemies based on the elapsed time (delta).
     *
     * @param delta The time in seconds since the last render.
     */
    public void update(float delta) {
        stateTime += delta;

        if (!isDestroyed && isSetToDestroy) {
            world.destroyBody(body);
            isDestroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!isDestroyed) {
            body.setLinearVelocity(velocity);
            setPosition(
                    body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    /**
     * Enemies change direction when they bump into each other
     *
     * @param userData The obtained userData object
     */
    @Override
    public void onEnemyHit(Enemy userData) {
        if (userData instanceof Turtle
                && ((Turtle) userData).getCurrentState() == TurtleState.MOVING_SHELL) {
            isSetToDestroy = true;
        } else {
            reverseVelocity(true, false);
        }
    }

    /**
     * Draw enemies
     *
     * @param batch The obtained Batch object
     */
    @Override
    public void draw(Batch batch) {
        if (!isDestroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    /** Create enemies */
    @Override
    protected void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.PPM);

        fixtureDef.filter.categoryBits = ObjectBit.ENEMY_BIT;
        fixtureDef.filter.maskBits =
                ObjectBit.DEFAULT_BIT
                        | ObjectBit.ENEMY_BIT
                        | ObjectBit.OBJECT_BIT
                        | ObjectBit.COIN_BIT
                        | ObjectBit.BRICK_BIT
                        | ObjectBit.MARIO_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        createHead(fixtureDef);

        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * Died when Mario stepped on his head
     *
     * @param mario Mario
     */
    @Override
    public void onHit(Mario mario) {
        isSetToDestroy = true;
        AudioLoader.stompSound.play();
    }

    /**
     * create enemy head
     *
     * @param fixtureDef The obtained fixtureDef object
     */
    private void createHead(FixtureDef fixtureDef) {
        defFixtureDef(fixtureDef);
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = ObjectBit.ENEMY_HEAD_BIT;
    }
}
