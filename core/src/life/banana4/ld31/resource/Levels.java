package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.FloorTile;
import life.banana4.ld31.Level;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.entity.Wall;
import life.banana4.ld31.util.TileType;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Levels extends ResourceBag<Level>
{
    public final Textures textures;

    public static final int TILE_WIDTH = 32;
    public static final int TILE_WIDTH_2 = TILE_WIDTH / 2;
    public static final int TILE_WIDTH_4 = TILE_WIDTH / 4;

    public Level level1;

    public Levels(Textures textures)
    {
        this.textures = textures;
    }

    @Override
    public void build()
    {
        textures.build();
        super.build();
    }

    @Override
    protected Level load(FileRef basedir, Field field)
    {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".bmp").getPath());
        TextureData map = new Texture(basedir.child(id + ".bmp").getPath()).getTextureData();
        map.prepare();
        Pixmap pixmap = map.consumePixmap();
        Level level = new Level(pixmap.getWidth(), pixmap.getHeight(), new TiledGraph(pixmap));

        int color;
        for (int x = 0; x < map.getWidth(); x++)
        {
            for (int y = 0; y < map.getHeight(); y++)
            {
                color = pixmap.getPixel(x, y);
                System.out.println(color);
                //System.out.println(TileType.getType(color));
                int xRegion = -1, yRegion = -1;
                switch (TileType.getType(color))
                {
                    case WALL:
                        level.addEntity(new Wall(TILE_WIDTH, TILE_WIDTH).move(x * TILE_WIDTH, y * TILE_WIDTH));
                        break;
                    case SNOW_TOP_LEFT:
                    xRegion = 0; yRegion = 2; break;
                    case SNOW_TOP:
                        xRegion = 1; yRegion = 2; break;
                    case SNOW_TOP_RIGHT:
                        xRegion = 2; yRegion = 2; break;
                    case SNOW_LEFT:
                        xRegion = 0; yRegion = 1; break;
                    case SNOW:
                        xRegion = 4; yRegion = 1; break;
                    case SNOW_RIGHT:
                        xRegion = 2; yRegion = 1; break;
                    case SNOW_BOTTOM_LEFT:
                        xRegion = 0; yRegion = 0; break;
                    case SNOW_BOTTOM:
                        xRegion = 1; yRegion = 0; break;
                    case SNOW_BOTTOM_RIGHT:
                        xRegion = 2; yRegion = 0; break;
                    case DIRT_TOP_LEFT:
                        xRegion = 3; yRegion = 2; break;
                    case DIRT_TOP:
                        xRegion = 4; yRegion = 2; break;
                    case DIRT_TOP_RIGHT:
                        xRegion = 5; yRegion = 2; break;
                    case DIRT_LEFT:
                        xRegion = 3; yRegion = 1; break;
                    case DIRT:
                        xRegion = 1; yRegion = 1; break;
                    case DIRT_RIGHT:
                        xRegion = 5; yRegion = 1; break;
                    case DIRT_BOTTOM_LEFT:
                        xRegion = 3; yRegion = 0; break;
                    case DIRT_BOTTOM:
                        xRegion = 4; yRegion = 0; break;
                    case DIRT_BOTTOM_RIGHT:
                        xRegion = 5; yRegion = 0; break;
                    case NONE:
                        System.out.println("No type for that color");
                        break;
                }
                if (xRegion != -1 && yRegion != -1)
                {
                    level.getFloor().add(new FloorTile(new TextureRegion(textures.floor, xRegion * TILE_WIDTH, yRegion * TILE_WIDTH, TILE_WIDTH,
                                                                         TILE_WIDTH)).setPosition(new Vector2(x * TILE_WIDTH, y * TILE_WIDTH)));
                }
            }
        }
        return level;
    }
}
