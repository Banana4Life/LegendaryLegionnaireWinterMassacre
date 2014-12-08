package life.banana4.ld31.resource;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.FloorTile;
import life.banana4.ld31.Ld31;
import life.banana4.ld31.Level;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.entity.Wall;
import life.banana4.ld31.util.TileType;

public class LevelLoader
{

    public static final int TILE_WIDTH = 32;
    public static final int TILE_WIDTH_2 = TILE_WIDTH / 2;

    public static Level load(Ld31 game, Textures textures)
    {
        Random random = new Random(System.currentTimeMillis());
        Pixmap map = new Pixmap(Gdx.files.internal("level.bmp"));
        Level level = new Level(game, map.getWidth(), map.getHeight(), new TiledGraph(map));

        int color;
        for (int x = 0; x < map.getWidth(); x++)
        {
            for (int y = 0; y < map.getHeight(); y++)
            {
                color = map.getPixel(x, y);
                int xRegion = -1, yRegion = -1;
                switch (TileType.getType(color))
                {
                    case WALL:
                        boolean flipX = random.nextInt(2) == 1;
                        boolean flipY = random.nextInt(2) == 1;
                        int number = random.nextInt(10);
                        level.addEntity(new Wall(new TextureRegion(textures.floor, (flipX ? TILE_WIDTH : 0)
                            + (number < 5 ? number < 2 ? 2 : 1 : 0) * TILE_WIDTH + (
                            color == 572536063 ? TILE_WIDTH * 3 : 0), (flipY ? TILE_WIDTH : 0) + 3 * TILE_WIDTH,
                                                                   flipX ? -TILE_WIDTH : TILE_WIDTH,
                                                                   flipY ? -TILE_WIDTH : TILE_WIDTH), TILE_WIDTH,
                                                 TILE_WIDTH).move(x * TILE_WIDTH, y * TILE_WIDTH));
                        break;
                    case SNOW_TOP_LEFT:
                        xRegion = 0;
                        yRegion = 2;
                        break;
                    case SNOW_TOP:
                        if (random.nextInt(2) == 1)
                        {
                            xRegion = 1;
                            yRegion = 2;
                        }
                        else
                        {
                            xRegion = 4;
                            yRegion = 0;
                        }
                        break;
                    case SNOW_TOP_RIGHT:
                        xRegion = 2;
                        yRegion = 2;
                        break;
                    case SNOW_LEFT:
                        xRegion = (random.nextInt(2) == 1) ? 0 : 5;
                        yRegion = 1;
                        break;
                    case SNOW:
                        xRegion = 4;
                        yRegion = 1;
                        break;
                    case SNOW_RIGHT:
                        xRegion = (random.nextInt(2) == 1) ? 2 : 3;
                        yRegion = 1;
                        break;
                    case SNOW_BOTTOM_LEFT:
                        xRegion = 0;
                        yRegion = 0;
                        break;
                    case SNOW_BOTTOM:
                        if (random.nextInt(2) == 1)
                        {
                            xRegion = 1;
                            yRegion = 0;
                        }
                        else
                        {
                            xRegion = 4;
                            yRegion = 2;
                        }
                        break;
                    case SNOW_BOTTOM_RIGHT:
                        xRegion = 2;
                        yRegion = 0;
                        break;
                    case DIRT_TOP_LEFT:
                        xRegion = 3;
                        yRegion = 2;
                        break;
                    case DIRT_TOP:
                        xRegion = 4;
                        yRegion = 2;
                        break;
                    case DIRT_TOP_RIGHT:
                        xRegion = 5;
                        yRegion = 2;
                        break;
                    case DIRT_LEFT:
                        xRegion = 3;
                        yRegion = 1;
                        break;
                    case DIRT:
                        xRegion = 1;
                        yRegion = 1;
                        break;
                    case DIRT_RIGHT:
                        xRegion = 5;
                        yRegion = 1;
                        break;
                    case DIRT_BOTTOM_LEFT:
                        xRegion = 3;
                        yRegion = 0;
                        break;
                    case DIRT_BOTTOM:
                        xRegion = 4;
                        yRegion = 0;
                        break;
                    case DIRT_BOTTOM_RIGHT:
                        xRegion = 5;
                        yRegion = 0;
                        break;
                    case NONE:
                        System.out.println("No type for that color: " + Integer.toHexString(color));
                        break;
                }
                if (xRegion != -1 && yRegion != -1)
                {
                    level.getFloor().add(new FloorTile(new TextureRegion(textures.floor, xRegion * TILE_WIDTH,
                                                                         yRegion * TILE_WIDTH, TILE_WIDTH,
                                                                         TILE_WIDTH)).setPosition(new Vector2(
                        x * TILE_WIDTH, y * TILE_WIDTH)));
                }
            }
        }
        return level;
    }
}
