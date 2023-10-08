package org.mario.object;

import com.badlogic.gdx.maps.MapObject;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;
import org.mario.sprites.player.MarioState;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class Flag extends InterMapObj {
    private float x;
    private float doorX;

    public Flag(InGameScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(ObjectBit.FLAG_BIT);
        x = body.getPosition().x; // Gets the x-coordinate of the flag
        doorX = x + 112f / Game.PPM; // Gets the x coordinate of the gate
    }

    /**
     * After Mario collides with the flagpole, rename Mario's position and orientation
     *
     * @param mario Mario
     */
    @Override
    public void onHit(Mario mario) {
        mario.setDoorX(doorX);
        if (mario.getCurrentState() == MarioState.CLIMBING) { // If Mario is in climbing mode
            float flagX = body.getPosition().x;
            mario.setFlagRight(!(mario.getX() < flagX)); // Set Mario to face the flag
        }
        mario.win();
    }
}
