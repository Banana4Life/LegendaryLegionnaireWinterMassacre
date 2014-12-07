package life.banana4.ld31.util;

import java.util.HashMap;
import java.util.Map;

public enum TileType
{
    WALL(255, false),
    SNOW_TOP_LEFT(-370546177, true),
    SNOW_TOP(-724249345, true),
    SNOW_TOP_RIGHT(-1077952513, true),
    SNOW_LEFT(-1431655681, true),
    SNOW(-1, true),
    SNOW_RIGHT(-1802201857, true),
    SNOW_BOTTOM_LEFT(2139062271, true),
    SNOW_BOTTOM(1785359103, true),
    SNOW_BOTTOM_RIGHT(1431655935, true),
    DIRT_TOP_LEFT(-118361601, true),
    DIRT_TOP(1, true),
    DIRT_TOP_RIGHT(-608585217, true),
    DIRT_LEFT(3, true),
    DIRT(-1082097409, true),
    DIRT_RIGHT(4, true),
    DIRT_BOTTOM_LEFT(-1672589569, true),
    DIRT_BOTTOM(6, true),
    DIRT_BOTTOM_RIGHT(1664234239, true),
    NONE(300, true);

    private static final Map<Integer, TileType> LOOKUP = new HashMap<>();
    private final int color;
    private final boolean walkable;

    TileType(int color, boolean walkable)
    {
        this.color = color;
        this.walkable = walkable;
    }

    public static TileType getType(int color)
    {
        return LOOKUP.get(color) == null ? NONE : LOOKUP.get(color);
    }

    public static boolean isFloor(TileType type)
    {
        return !(type == WALL || type == NONE);
    }

    static
    {
        for (final TileType tileType : values())
        {
            LOOKUP.put(tileType.color, tileType);
        }
    }

    public boolean isWalkable()
    {
        return walkable;
    }
}
