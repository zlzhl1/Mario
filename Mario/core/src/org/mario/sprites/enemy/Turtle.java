package org.mario.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class Turtle extends Enemy {
    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;
    float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private TextureRegion shell;
    private Array<TextureRegion> frames;
    private boolean isDestroyed;
    private boolean isSetToDestroy;
    private float deadRotationDegrees;
    private TurtleState currentState;
    private TurtleState previousState;

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Turtle(InGameScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        walkAnimation = new Animation<TextureRegion>(0.2f, frames);
        currentState = previousState = TurtleState.WALKING;
        deadRotationDegrees = 0;

        setBounds(getX(), getY(), 16 / Game.PPM, 24 / Game.PPM);
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
     * Mario stepped on his head
     *
     * @param mario Mario
     */
    @Override
    public void onHit(Mario mario) {
        if (currentState != TurtleState.STANDING_SHELL) {
            currentState = TurtleState.STANDING_SHELL;
            velocity.x = 0;
        } else {
            kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
        }
    }

    public TurtleState getCurrentState() {
        return currentState;
    }

    /**
     * The direction of movement of the shell
     *
     * @param direction turtle direction
     */
    public void kick(int direction) {
        velocity.x = direction;
        currentState = TurtleState.MOVING_SHELL;
    }

    /**
     * This method updates enemies based on the elapsed time (delta).
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void update(float delta) {
        setRegion(getFrame(delta));
        if (currentState == TurtleState.STANDING_SHELL && stateTime > 5) {
            currentState = TurtleState.WALKING;
            velocity.x = 1;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8 / Game.PPM);

        if (currentState == TurtleState.DEAD) {
            deadRotationDegrees += 3;
            rotate(deadRotationDegrees);
            if (stateTime > 5 && !isDestroyed) {
                world.destroyBody(body);
                isDestroyed = true;
            } else if (isDestroyed) {
                setRegion(shell);
            }
        } else body.setLinearVelocity(velocity); // set velocity to body
    }

    /**
     * When enemies collide, they have different effects depending on the turtle's state
     *
     * @param userData The obtained userData object
     */
    @Override
    public void onEnemyHit(Enemy userData) {
        if (userData instanceof Turtle) {
            if (((Turtle) userData).getCurrentState() == TurtleState.MOVING_SHELL
                    && currentState != TurtleState.MOVING_SHELL) {
                killed();
            } else if (currentState == TurtleState.MOVING_SHELL
                    && ((Turtle) userData).getCurrentState() == TurtleState.WALKING) {
                return;
            } else {
                reverseVelocity(true, false);
            }
        } else if (currentState != TurtleState.MOVING_SHELL) {
            reverseVelocity(true, false);
        }
    }

    /** Turtle death */
    private void killed() {
        currentState = TurtleState.DEAD;
        Filter filter = new Filter();
        filter.maskBits = ObjectBit.NOTHING_BIT;

        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
    }

    /**
     * Returns the current frame of the animation based on the entity's state and velocity
     *
     * @param delta The time in seconds since the last render.
     * @return TextureRegion
     */
    private TextureRegion getFrame(float delta) {
        TextureRegion region;
        switch (currentState) {
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (velocity.x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (velocity.x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;
        return region;
    }

    /**
     * create enemy head
     *
     * @param fixtureDef The obtained fixtureDef object
     */
    private void createHead(FixtureDef fixtureDef) {
        defFixtureDef(fixtureDef);
        fixtureDef.restitution = 1.5f;
        fixtureDef.filter.categoryBits = ObjectBit.ENEMY_HEAD_BIT;
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
}
