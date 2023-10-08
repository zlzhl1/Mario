package org.mario.object;

import com.badlogic.gdx.maps.MapObject;
import org.mario.hud.Hud;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;
import org.mario.utils.AudioLoader;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 *     <p>Brick class represents a brick object in the game world. It extends the InterMapObj class
 *     and implements the onHit() method to handle collision events with Mario. When Mario hits a
 *     brick, onHit() method checks if Mario is big or not. If Mario is big, the brick is destroyed,
 *     and the player gets 200 points added to the score. If Mario is small, the bump sound is
 *     played. This class also sets the category filter for the brick object.
 */
public class Brick extends InterMapObj {
    public Brick(InGameScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(ObjectBit.BRICK_BIT);
    }

    /**
     * Colliding with Mario
     *
     * @param mario Mario
     */
    @Override
    public void onHit(Mario mario) {
        if (mario.isBig()) { // Determine Mario's status
            setCategoryFilter(ObjectBit.DESTROYED_BIT);
            getTiledMapTileLayerCell().setTile(null);
            AudioLoader.breakBlockSound.play();
            Hud.addScore(200);
        } else {
            AudioLoader.bumpSound.play();
        }
    }
}
