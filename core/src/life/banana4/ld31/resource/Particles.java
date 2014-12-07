package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Particles extends ResourceBag<ParticleEffect>
{
    public ParticleEffect explosion;

    @Override
    protected ParticleEffect load(FileRef basedir, Field field)
    {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(fieldToFileRef(field, basedir).getPath() + ".p"), Gdx.files.internal("particles"));
        return effect;
    }
}
