package life.banana4.ld31.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Sounds extends ResourceBag<Sound>
{
    public Sound aliendeath;
    public Sound rocketdeath;
    public Sound swordswing;
    public Sound crossbow;
    public Sound fireballin;
    public Sound fireballout;
    public Sound legionhit;
    public Sound unicorndeath;
    public Sound laser;

    @Override
    protected Sound load(FileRef basedir, Field field)
    {
        String ext = "wav";
        Ext annotation = field.getAnnotation(Ext.class);
        if (annotation != null)
        {
            ext = annotation.value();
        }
        return Gdx.audio.newSound(Gdx.files.internal(fieldToFileRef(field, basedir).getPath() + "." + ext));
    }

    @Override
    public void build()
    {
        super.build();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Ext
    {
        public String value();
    }
}
