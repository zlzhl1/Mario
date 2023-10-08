package org.mario.sprites.items;

import com.badlogic.gdx.math.Vector2;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    /**
     * Constructor for ItemDef.
     *
     * @param position The position of the item in 2D vector.
     * @param type The type of the item as a class.
     */
    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
