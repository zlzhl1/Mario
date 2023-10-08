package org.mario.object;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.mario.Game;
import org.mario.screens.InGameScreen;
import org.mario.sprites.player.Mario;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public abstract class InterMapObj {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected InGameScreen screen;
    protected MapObject mapObject;

    public InterMapObj(InGameScreen screen, MapObject mapObject) {
        // Initializes each property
        this.mapObject = mapObject;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) mapObject).getRectangle();
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (bounds.getX() + bounds.getWidth() / 2) / Game.PPM,
                (bounds.getY() + bounds.getHeight() / 2) / Game.PPM);

        body = world.createBody(bodyDef); // Initializes Box2D object

        shape.setAsBox(bounds.getWidth() / 2 / Game.PPM, bounds.getHeight() / 2 / Game.PPM);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef); // Initializes Box2D fixture
    }

    public abstract void onHit(Mario mario);

    /**
     * This function is used to set the filter of the fixture so that it can be distinguished from
     * other fixtures.
     *
     * @param filterBit categoryBits
     */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    /**
     * Used to get the TiledMapTileLayer.Cell object, which represents the position of the
     * interactive object in the TiledMap.
     *
     * @return TiledMapTileLayer.Cell
     */
    public TiledMapTileLayer.Cell getTiledMapTileLayerCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell(
                (int) (body.getPosition().x * Game.PPM / 16),
                (int) (body.getPosition().y * Game.PPM / 16));
    }
}
