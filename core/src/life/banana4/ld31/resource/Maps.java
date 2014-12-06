package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import java.util.List;
import life.banana4.ld31.FloorTile;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Maps extends ResourceBag<List<FloorTile>>
{
    @Override
    protected List<FloorTile> load(FileRef basedir, Field field)
    {
        return null;
    }
}
