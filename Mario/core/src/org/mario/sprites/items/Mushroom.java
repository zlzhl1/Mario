package org.mario.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
public class Mushroom extends Item {

    /**
     * Constructor
     *
     * @param screen The obtained InGameScreen object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Mushroom(InGameScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    /** This method defines the item's body using Box2D. */
    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.PPM);

        fixtureDef.filter.categoryBits = ObjectBit.ITEM_BIT;
        fixtureDef.filter.maskBits =
                ObjectBit.MARIO_BIT
                        | ObjectBit.OBJECT_BIT
                        | ObjectBit.DEFAULT_BIT
                        | ObjectBit.COIN_BIT
                        | ObjectBit.BRICK_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * This method is called when Mario picks up the mushroom. It destroys the mushroom and makes
     * Mario grow.
     *
     * @param mario The Mario object that picks up the mushroom.
     */
    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
    }

    /**
     * This method updates the mushroom's position and velocity.
     *
     * @param dt The time step.
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
