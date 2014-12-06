package life.banana4.ld31.util;

import java.util.HashMap;
import java.util.Map;

public enum TileType
{
    WALL(255),
    FLOOR(-1),
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

    static
    {
        for (final TileType tileType : values())
        {
            LOOKUP.put(tileType.color, tileType);
        }
    }
}
