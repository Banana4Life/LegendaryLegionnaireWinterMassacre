package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Songs extends ResourceBag<Music>
{
    public Music main;

    @Override
    protected Music load(FileRef basedir, Field field) {
        return Gdx.audio.newMusic(Gdx.files.internal(fieldToFileRef(field, basedir).getPath() + ".mp3"));
    }
}
