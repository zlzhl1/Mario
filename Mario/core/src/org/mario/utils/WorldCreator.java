package org.mario.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.mario.Game;
import org.mario.object.Brick;
import org.mario.object.Coin;
import org.mario.object.Flag;
import org.mario.screens.InGameScreen;
import org.mario.sprites.enemy.Enemy;
import org.mario.sprites.enemy.Goomba;
import org.mario.sprites.enemy.Turtle;

/**
 * This class is responsible for creating objects in the game world based on the TiledMap. It
 * creates ground objects, pipe objects, brick objects, coin objects, enemy objects and flag
 * objects. This class is used in the InGameScreen class.
 *
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    /**
     * Constructor
     *
     * @param screen Game screen
     */
    public WorldCreator(InGameScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        createGroundObjects(world, map);
        createPipeObjects(world, map);
        createBrickObjects(screen, map);
        createCoinObjects(screen, map);
        createGoombaEnemies(screen, map);
        createTurtleEnemies(screen, map);
        createFlagObjects(screen, map);
    }

    /**
     * Create ground objects in the game world based on the TiledMap.
     *
     * @param world The game world
     * @param map The TiledMap
     */
    private void createGroundObjects(World world, TiledMap map) {
        for (MapObject mapObject :
                map.getLayers()
                        .get(MapLayer.GROUND_LAYER)
                        .getObjects()
                        .getByType(MapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rectangle.getX() + rectangle.getWidth() / 2) / Game.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / Game.PPM);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    rectangle.getWidth() / 2 / Game.PPM, rectangle.getHeight() / 2 / Game.PPM);
            FixtureDef fixTureDef = new FixtureDef();
            fixTureDef.shape = shape;
            body.createFixture(fixTureDef);
        }
    }

    /**
     * Create pipe objects in the game world based on the TiledMap.
     *
     * @param world The game world
     * @param map The TiledMap
     */
    private void createPipeObjects(World world, TiledMap map) {
        for (MapObject mapObject :
                map.getLayers().get(MapLayer.PIPE_LAYER).getObjects().getByType(MapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rectangle.getX() + rectangle.getWidth() / 2) / Game.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / Game.PPM);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    rectangle.getWidth() / 2 / Game.PPM, rectangle.getHeight() / 2 / Game.PPM);
            FixtureDef fixTureDef = new FixtureDef();
            fixTureDef.shape = shape;
            fixTureDef.filter.categoryBits = ObjectBit.OBJECT_BIT;
            body.createFixture(fixTureDef);
        }
    }

    /**
     * Create brick objects in the game world based on the TiledMap.
     *
     * @param screen The game screen
     * @param map The TiledMap
     */
    private void createBrickObjects(InGameScreen screen, TiledMap map) {
        for (MapObject mapObject :
                map.getLayers().get(MapLayer.BRICK_LAYER).getObjects().getByType(MapObject.class)) {
            new Brick(screen, mapObject);
        }
    }

    /**
     * Create coin objects in the game world based on the TiledMap.
     *
     * @param screen The game screen
     * @param map The TiledMap
     */
    private void createCoinObjects(InGameScreen screen, TiledMap map) {
        for (MapObject mapObject :
                map.getLayers().get(MapLayer.COIN_LAYER).getObjects().getByType(MapObject.class)) {
            new Coin(screen, mapObject);
        }
    }

    /**
     * Create enemies in the game world based on the TiledMap.
     *
     * @param screen The game screen
     * @param map The TiledMap
     */
    private void createGoombaEnemies(InGameScreen screen, TiledMap map) {
        goombas = new Array<>();
        for (MapObject mapObject :
                map.getLayers()
                        .get(MapLayer.GOOMBA_LAYER)
                        .getObjects()
                        .getByType(MapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            goombas.add(
                    new Goomba(screen, rectangle.getX() / Game.PPM, rectangle.getY() / Game.PPM));
        }
    }

    /**
     * Create enemies in the game world based on the TiledMap.
     *
     * @param screen The game screen
     * @param map The TiledMap
     */
    private void createTurtleEnemies(InGameScreen screen, TiledMap map) {
        turtles = new Array<>();
        for (MapObject mapObject :
                map.getLayers()
                        .get(MapLayer.TURTLE_LAYER)
                        .getObjects()
                        .getByType(MapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            turtles.add(
                    new Turtle(screen, rectangle.getX() / Game.PPM, rectangle.getY() / Game.PPM));
        }
    }

    /**
     * Create flag in the game world based on the TiledMap.
     *
     * @param screen The game screen
     * @param map The TiledMap
     */
    private void createFlagObjects(InGameScreen screen, TiledMap map) {
        for (MapObject mapObject :
                map.getLayers().get(MapLayer.FLAG_LAYER).getObjects().getByType(MapObject.class)) {
            new Flag(screen, mapObject);
        }
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
