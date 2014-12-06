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

    public Levels(Textures textures) {
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
        for (int x = 0; x < map.getWidth(); x++)
        {
            for (int y = 0; y < map.getHeight(); y++)
            {
                int color = pixmap.getPixel(x, y);
                System.out.println(color);
                //System.out.println(TileType.getType(color));
                switch (TileType.getType(color))
                {
                    case WALL:
                        level.addEntity(new Wall(TILE_WIDTH, TILE_WIDTH).move(x * TILE_WIDTH, y * TILE_WIDTH));
                        break;
                    case SNOW:
                        level.getFloor().add(new FloorTile(new TextureRegion(textures.floor, 4 * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH)).setPosition(new Vector2(x * TILE_WIDTH, y * TILE_WIDTH)));
                        break;
                    case DIRT:
                        level.getFloor().add(new FloorTile(new TextureRegion(textures.floor, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, TILE_WIDTH)).setPosition(new Vector2(x * TILE_WIDTH, y * TILE_WIDTH)));
                    case NONE:
                        System.out.println("No type for that color");
                        break;
                }
            }
        }
        return level;
    }
}
