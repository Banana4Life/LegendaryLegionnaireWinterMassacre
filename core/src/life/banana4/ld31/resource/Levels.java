package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import life.banana4.ld31.Level;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.entity.Wall;
import life.banana4.ld31.util.TileType;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Levels extends ResourceBag<Level>
{
    public static final int TILE_WIDTH = 32;

    public Level level1;

    @Override
    protected Level load(FileRef basedir, Field field)
    {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".bmp").getPath());
        TextureData map = new Texture(basedir.child(id + ".bmp").getPath()).getTextureData();
        map.prepare();
        Pixmap pixmap = map.consumePixmap();
        Level level = new Level(pixmap.getWidth(), pixmap.getHeight(), TILE_WIDTH, new TiledGraph(pixmap));
        for (int x = 0; x < map.getWidth(); x++)
        {
            for (int y = 0; y < map.getHeight(); y++)
            {
                int color = pixmap.getPixel(x, y);
                //System.out.println(color);
                //System.out.println(TileType.getType(color));
                switch (TileType.getType(color))
                {
                    case WALL:
                        level.addEntity(new Wall(TILE_WIDTH, TILE_WIDTH));
                        break;
                    case NONE:
                        System.out.println("No type for that color");
                        break;
                }
            }
        }
        return level;
    }
}
