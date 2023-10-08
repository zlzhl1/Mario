package org.mario.sprites.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.enemy.Enemy;
import org.mario.sprites.enemy.Turtle;
import org.mario.sprites.enemy.TurtleState;
import org.mario.utils.AudioLoader;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class Mario extends Sprite {
    private World world;
    private Body body;
    private TextureRegion marioStand;
    private MarioState currentState;
    private MarioState previousState;
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;
    private boolean isBig;
    private boolean runGrowAnimation;
    private boolean isRight;
    private boolean toDefineBigMario;
    private boolean toReDefineMario;
    private boolean isWin;
    private boolean isClimbing;
    private boolean isDead;
    private boolean flagRight;
    private float doorX;
    private float stateTimer;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDied;
    private TextureRegion marioClimbing;
    private TextureRegion bigMarioClimbing;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;
    private boolean isJumping = false;

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     */
    public Mario(InGameScreen screen) {
        this.world = screen.getWorld();

        currentState = MarioState.STANDING;
        previousState = MarioState.STANDING;
        stateTimer = 0;
        isRight = true;
        isBig = false;
        isWin = false;
        isClimbing = false;
        flagRight = false;

        marioRun = createAnimation(screen, "little_mario", 3, 16, 16, 0.1f);
        bigMarioRun = createAnimation(screen, "big_mario", 3, 16, 32, 0.1f);

        Array<TextureRegion> growFrames = new Array<>();
        growFrames.add(
                new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        growFrames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growFrames.add(
                new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        growFrames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation<>(0.2f, growFrames);

        marioJump = createTextureRegion(screen, "little_mario", 80, 0, 16, 16);
        bigMarioJump = createTextureRegion(screen, "big_mario", 80, 0, 16, 32);

        marioDied = createTextureRegion(screen, "little_mario", 96, 0, 16, 16);

        createMario();

        marioStand = createTextureRegion(screen, "little_mario", 0, 0, 16, 16);
        bigMarioStand = createTextureRegion(screen, "big_mario", 0, 0, 16, 32);
        marioClimbing = createTextureRegion(screen, "little_mario", 128, 0, 16, 16);
        bigMarioClimbing = createTextureRegion(screen, "big_mario", 128, 0, 16, 32);

        setBounds(0, 0, 16 / Game.PPM, 16 / Game.PPM);
        setRegion(marioStand);
    }

    /** creat mario */
    private void createMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / Game.PPM, 32 / Game.PPM);
        createBody(bodyDef);
    }

    /** creat big mario */
    private void createBigMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, 10 / Game.PPM));
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / Game.PPM);

        createShape(fixtureDef, shape);
        shape.setPosition(new Vector2(0, -14 / Game.PPM));
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Game.PPM, 5 / Game.PPM), new Vector2(2 / Game.PPM, 5 / Game.PPM));

        fixtureDef.filter.categoryBits = ObjectBit.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
        toDefineBigMario = false;
    }

    /** redefine Mario */
    private void redefineMario() {
        Vector2 position = body.getPosition();
        BodyDef bodyDef = new BodyDef();

        world.destroyBody(body);

        bodyDef.position.set(position);
        createBody(bodyDef);

        toReDefineMario = false;
    }

    /**
     * This method updates the mario's position, velocity and status.
     *
     * @param delta The time step.
     */
    public void update(float delta) {
        if (isWin) {
            updateWinning(delta);
            return;
        }

        if (isBig) {
            setPosition(
                    body.getPosition().x - getWidth() / 2,
                    body.getPosition().y - getHeight() / 2 - 6 / Game.PPM);
        } else {
            setPosition(
                    body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
        setRegion(getFrame(delta));

        if (toDefineBigMario) {
            createBigMario();
        }
        if (toReDefineMario) {
            redefineMario();
        }
    }

    /**
     * Returns the current frame of the animation based on the entity's state and velocity
     *
     * @param delta The time in seconds since the last render.
     * @return TextureRegion
     */
    public TextureRegion getFrame(float delta) {
        currentState = getCurrentState();
        TextureRegion region;
        boolean flipX = false;
        switch (currentState) {
            case CLIMBING:
                region = isBig ? bigMarioClimbing : marioClimbing;
                if (flagRight && !region.isFlipX()) {
                    flipX = true;
                } else if (!flagRight && region.isFlipX()) {
                    flipX = true;
                }
                break;
            case DEAD:
                region = marioDied;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = isBig ? bigMarioJump : marioJump;
                break;
            case WIN:
                region =
                        body.getLinearVelocity().x != 0
                                ? (isBig ? bigMarioStand : marioStand)
                                : getRunningRegion();
                break;
            case RUNNING:
                region = getRunningRegion();
                break;
            case FALLING:
            case STANDING:
            default:
                region = isBig ? bigMarioStand : marioStand;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !isRight) && !region.isFlipX()) {
            flipX = true;
            isRight = false;
        } else if ((body.getLinearVelocity().x > 0 || isRight) && region.isFlipX()) {
            flipX = true;
            isRight = true;
        }

        if (flipX) {
            region.flip(true, false);
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    /**
     * Returns the current frame of the animation based on the entity's state and velocity
     *
     * @return TextureRegion
     */
    private TextureRegion getRunningRegion() {
        return isBig
                ? bigMarioRun.getKeyFrame(stateTimer, true)
                : marioRun.getKeyFrame(stateTimer, true);
    }

    /**
     * Returns the current frame of the animation based on the entity's state and velocity
     *
     * @return TextureRegion
     */
    public MarioState getCurrentState() {
        if (isClimbing) {
            return MarioState.CLIMBING;
        } else if (isWin) {
            return MarioState.WIN;
        } else if (isDead) {
            return MarioState.DEAD;
        } else if (runGrowAnimation) {
            return MarioState.GROWING;
        } else if (body.getLinearVelocity().y > 0
                || (body.getLinearVelocity().y < 0 && previousState == MarioState.JUMPING)) {
            return MarioState.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return MarioState.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return MarioState.RUNNING;
        } else {
            return MarioState.STANDING;
        }
    }

    /**
     * When Mario encounters an enemy, his status will drop one level and he will die if he is not
     * in Big Mario status
     *
     * @param enemy enemy
     */
    public void hit(Enemy enemy) {
        if (enemy instanceof Turtle
                && ((Turtle) enemy).getCurrentState() == TurtleState.STANDING_SHELL) {
            ((Turtle) enemy)
                    .kick(
                            this.getX() <= enemy.getX()
                                    ? Turtle.KICK_RIGHT_SPEED
                                    : Turtle.KICK_LEFT_SPEED);
        } else {
            if (isBig) {
                runGrowAnimation = true;
                isBig = false;
                toReDefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                AudioLoader.stompSound.play();
            } else {
                AudioLoader.marioDieSound.play();
                isDead = true;
                Filter filter = new Filter();
                filter.maskBits = ObjectBit.NOTHING_BIT;
                for (Fixture fixture : body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
            }
        }
    }

    /** Mario Victory */
    public void win() {
        isWin = true;
        isClimbing = true;
        body.setLinearVelocity(0, 0);
        stateTimer = 0;
    }

    private boolean isExecuted = false;

    /**
     * Mario will automatically move to the gate when he meets the conditions for victory
     *
     * @param dt The time in seconds since the last render.
     */
    private void updateWinning(float dt) {
        if (stateTimer < 1.5f) {
            body.setLinearVelocity(0, -2);
            setPosition(
                    body.getPosition().x - getWidth() / 2,
                    body.getPosition().y - getHeight() / 2 - (isBig ? 6 : 0) / Game.PPM);
        } else if (stateTimer < 1.8f) {
            isClimbing = false;
            setRegion(getFrame(dt));
        } else if (stateTimer < 2f) {
            if (!isExecuted) {
                Filter filter = new Filter();
                filter.maskBits = ObjectBit.DEFAULT_BIT;
                for (Fixture fixture : body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                isExecuted = true;
            }
            setPosition(
                    body.getPosition().x - getWidth() / 2,
                    body.getPosition().y - getHeight() / 2 - (isBig ? 6 : 0) / Game.PPM);
        } else if (stateTimer < 4.5f || body.getPosition().x != doorX) {
            body.setLinearVelocity(0.4F, 0);
            TextureRegion currentFrame =
                    isBig
                            ? bigMarioRun.getKeyFrame(stateTimer, true)
                            : marioRun.getKeyFrame(stateTimer, true);
            setRegion(currentFrame);
            if (body.getPosition().x - getWidth() / 2 != doorX - getWidth() / 2) {
                setPosition(
                        body.getPosition().x - getWidth() / 2 + 8 / Game.PPM,
                        body.getPosition().y - getHeight() / 2 - (isBig ? 6 : 0) / Game.PPM);
            }
        } else {
            assert body.getPosition().x - getWidth() / 2 == doorX - getWidth() / 2;
            setRegion(getFrame(dt));
        }
        stateTimer += dt;
    }

    /**
     * Create an animation
     *
     * @param screen Game screen object
     * @param regionName Texture region name
     * @param frameCount Number of frames
     * @param frameWidth Frame width
     * @param frameHeight Frame height
     * @param frameDuration Duration per frame
     * @return Returns a texture area animation object
     */
    private Animation<TextureRegion> createAnimation(
            InGameScreen screen,
            String regionName,
            int frameCount,
            int frameWidth,
            int frameHeight,
            float frameDuration) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < frameCount + 1; i++) {
            frames.add(
                    new TextureRegion(
                            screen.getAtlas().findRegion(regionName),
                            i * frameWidth,
                            0,
                            frameWidth,
                            frameHeight));
        }
        return new Animation<>(frameDuration, frames);
    }

    /**
     * @param screen Game screen object
     * @param regionName Texture region name
     * @param x co-ordinate
     * @param y co-ordinate
     * @param width width
     * @param height height
     * @return TextureRegion object
     */
    private TextureRegion createTextureRegion(
            InGameScreen screen, String regionName, int x, int y, int width, int height) {
        return new TextureRegion(screen.getAtlas().findRegion(regionName), x, y, width, height);
    }

    /**
     * Create a Box2D body
     *
     * @param bodyDef BodyDef
     */
    private void createBody(BodyDef bodyDef) {
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.PPM);

        createShape(fixtureDef, shape);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Game.PPM, 6 / Game.PPM), new Vector2(2 / Game.PPM, 6 / Game.PPM));
        fixtureDef.filter.categoryBits = ObjectBit.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * Create a circular Shape and set it up as a fixture for the rigid body for physics
     *
     * @param fixtureDef A FixtureDef object
     * @param shape shape
     */
    private void createShape(FixtureDef fixtureDef, CircleShape shape) {
        fixtureDef.filter.categoryBits = ObjectBit.MARIO_BIT;
        fixtureDef.filter.maskBits =
                ObjectBit.COIN_BIT
                        | ObjectBit.BRICK_BIT
                        | ObjectBit.DEFAULT_BIT
                        | ObjectBit.ENEMY_BIT
                        | ObjectBit.OBJECT_BIT
                        | ObjectBit.ENEMY_HEAD_BIT
                        | ObjectBit.ITEM_BIT
                        | ObjectBit.FLAG_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /** Change Mario's status to Big */
    public void grow() {
        if (!isBig) {
            runGrowAnimation = true;
            isBig = true;
            toDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            AudioLoader.powerUpSound.play();
        }
    }

    public Body getBody() {
        return body;
    }

    public boolean isBig() {
        return isBig;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void setFlagRight(boolean flagRight) {
        this.flagRight = flagRight;
    }

    public void setDoorX(float doorX) {
        this.doorX = doorX;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean getIsJumping() {
        return isJumping;
    }
}
