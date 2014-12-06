package life.banana4.ld31.resource;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

import java.lang.reflect.Field;

public class Tilemaps extends ResourceBag<StaticTiledMapTile> {
    public StaticTiledMapTile floor;

    @Override
    protected StaticTiledMapTile load(FileRef basedir, Field field) {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".png").getPath());
        return new StaticTiledMapTile(new TextureRegion(new Texture(basedir.child(id + ".png").getPath())));
    }
}
