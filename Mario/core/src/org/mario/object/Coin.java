package org.mario.object;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import org.mario.Game;
import org.mario.hud.Hud;
import org.mario.screens.InGameScreen;
import org.mario.sprites.items.ItemDef;
import org.mario.sprites.items.Mushroom;
import org.mario.sprites.player.Mario;
import org.mario.utils.AudioLoader;
import org.mario.utils.ObjectBit;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 *     <p>The Coin class represents a coin object in the game. It extends the InterMapObj class,
 *     which is a base class for all interactive map objects in the game. It contains methods for
 *     handling collision with Mario and updating the score.
 */
public class Coin extends InterMapObj {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN_MAP1 = 28; // The id of the blank coin tile for map 1-1
    private final int BLANK_COIN_MAP2 = 70; // The id of the blank coin tile for map 1-2
    private int BLANK_COIN;

    public Coin(InGameScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        tileSet =
                map.getTileSets()
                        .getTileSet(
                                "tileset_gutter"); // Gets the tileset_gutter block set and assigns
        // it to the tileSet variable
        fixture.setUserData(this);
        setCategoryFilter(ObjectBit.COIN_BIT);
        BLANK_COIN =
                screen.getCurrentMap().equals("1-1")
                        ? BLANK_COIN_MAP1
                        : BLANK_COIN_MAP2; // Select a BLANK COIN based on the current map
    }

    /**
     * This function is called when Mario collides with coins
     *
     * @param mario Mario
     */
    @Override
    public void onHit(Mario mario) {
        if (getTiledMapTileLayerCell().getTile().getId() == BLANK_COIN) { // Blank coin
            AudioLoader.bumpSound.play();
        } else {
            if (mapObject.getProperties().containsKey("mushroom")) { // mushroom
                screen.spawnItem(
                        new ItemDef(
                                new Vector2(
                                        body.getPosition().x, body.getPosition().y + 16 / Game.PPM),
                                Mushroom.class));
                AudioLoader.powerUpSound.play();
            } else { // coin
                AudioLoader.coinSound.play();
            }
        }
        getTiledMapTileLayerCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(200);
    }
}
