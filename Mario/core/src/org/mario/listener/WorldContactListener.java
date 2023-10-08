package org.mario.listener;

import com.badlogic.gdx.physics.box2d.*;
import org.mario.object.Flag;
import org.mario.object.InterMapObj;
import org.mario.sprites.enemy.Enemy;
import org.mario.sprites.items.Item;
import org.mario.sprites.player.Mario;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */

/**
 * This class is a contact listener for the game's physics engine using the Box2D library. The
 * beginContact() method is called when two fixtures start to touch. It determines the collision
 * type based on the category bits of the fixtures and calls the appropriate onHit() or hit() method
 * of the collided objects.
 */
public class WorldContactListener implements ContactListener {
    /**
     * Called when two fixtures begin to touch.
     *
     * @param contact Contact
     */
    @Override
    public void beginContact(Contact contact) {
        // Get two fixtures
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Get the collision category bits
        int collisionDef =
                fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        assert fixtureA.getUserData() != null;
        assert fixtureB.getUserData() != null;

        switch (collisionDef) {
                // Mario's head collides with bricks and coins
            case ObjectBit.MARIO_HEAD_BIT | ObjectBit.BRICK_BIT:
            case ObjectBit.MARIO_HEAD_BIT | ObjectBit.COIN_BIT:
                ((InterMapObj)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.MARIO_HEAD_BIT
                                                ? fixtureB
                                                : fixtureA)
                                        .getUserData())
                        .onHit(
                                (Mario)
                                        (fixtureA.getFilterData().categoryBits
                                                                == ObjectBit.MARIO_HEAD_BIT
                                                        ? fixtureA
                                                        : fixtureB)
                                                .getUserData());
                break;
                // Mario stepped on the enemy's head
            case ObjectBit.MARIO_BIT | ObjectBit.ENEMY_HEAD_BIT:
                ((Enemy)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.MARIO_BIT
                                                ? fixtureB
                                                : fixtureA)
                                        .getUserData())
                        .onHit(
                                (Mario)
                                        (fixtureA.getFilterData().categoryBits
                                                                == ObjectBit.MARIO_BIT
                                                        ? fixtureA
                                                        : fixtureB)
                                                .getUserData());
                break;
                // Enemies collide with items in the map
            case ObjectBit.ENEMY_BIT | ObjectBit.OBJECT_BIT:
            case ObjectBit.ENEMY_BIT | ObjectBit.DEFAULT_BIT:
                ((Enemy)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.ENEMY_BIT
                                                ? fixtureA
                                                : fixtureB)
                                        .getUserData())
                        .reverseVelocity(true, false);
                break;
                // Mario collides with enemy body parts
            case ObjectBit.MARIO_BIT | ObjectBit.ENEMY_BIT:
                ((Mario)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.MARIO_BIT
                                                ? fixtureA
                                                : fixtureB)
                                        .getUserData())
                        .hit(
                                (Enemy)
                                        (fixtureA.getFilterData().categoryBits
                                                                == ObjectBit.MARIO_BIT
                                                        ? fixtureB
                                                        : fixtureA)
                                                .getUserData());
                break;
                // The collision between enemies
            case ObjectBit.ENEMY_BIT:
                ((Enemy) fixtureA.getUserData()).onEnemyHit((Enemy) fixtureB.getUserData());
                ((Enemy) fixtureB.getUserData()).onEnemyHit((Enemy) fixtureA.getUserData());
                break;
                // Collision between items in the map
            case ObjectBit.ITEM_BIT | ObjectBit.OBJECT_BIT:
            case ObjectBit.ITEM_BIT | ObjectBit.DEFAULT_BIT:
                ((Item)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.ITEM_BIT
                                                ? fixtureA
                                                : fixtureB)
                                        .getUserData())
                        .reverseVelocity(true, false);
                break;
                // Collision between items and mario
            case ObjectBit.ITEM_BIT | ObjectBit.MARIO_BIT:
                ((Item)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.ITEM_BIT
                                                ? fixtureA
                                                : fixtureB)
                                        .getUserData())
                        .use(
                                (Mario)
                                        (fixtureA.getFilterData().categoryBits == ObjectBit.ITEM_BIT
                                                        ? fixtureB
                                                        : fixtureA)
                                                .getUserData());
                break;
                // Collision between flag and mario
            case ObjectBit.MARIO_BIT | ObjectBit.FLAG_BIT:
                ((Flag)
                                (fixtureA.getFilterData().categoryBits == ObjectBit.MARIO_BIT
                                                ? fixtureB
                                                : fixtureA)
                                        .getUserData())
                        .onHit(
                                (Mario)
                                        (fixtureA.getFilterData().categoryBits
                                                                == ObjectBit.MARIO_BIT
                                                        ? fixtureA
                                                        : fixtureB)
                                                .getUserData());
                break;
        }
    }

    /**
     * Called when two fixtures cease to touch.
     *
     * @param contact Contact
     */
    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
