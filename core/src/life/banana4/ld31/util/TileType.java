package life.banana4.ld31.util;

import java.util.HashMap;
import java.util.Map;

public enum TileType
{
    WALL(255),
    SNOW_TOP_LEFT(-370546177),
    SNOW_TOP(-724249345),
    SNOW_TOP_RIGHT(-1077952513),
    SNOW_LEFT(-1431655681),
    SNOW(-1),
    SNOW_RIGHT(-1802201857),
    SNOW_BOTTOM_LEFT(2139062271),
    SNOW_BOTTOM(1785359103),
    SNOW_BOTTOM_RIGHT(1431655935),
    DIRT_TOP_LEFT(-118361601),
    DIRT_TOP(1),
    DIRT_TOP_RIGHT(-608585217),
    DIRT_LEFT(3),
    DIRT(-1082097409),
    DIRT_RIGHT(4),
    DIRT_BOTTOM_LEFT(-1672589569),
    DIRT_BOTTOM(6),
    DIRT_BOTTOM_RIGHT(1664234239),
    NONE(300);

    private static final Map<Integer, TileType> LOOKUP = new HashMap<>();
    private final int color;

    TileType(int color)
    {
        this.color = color;
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
}
