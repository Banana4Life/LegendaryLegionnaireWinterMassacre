package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Particles extends ResourceBag<ParticleEffectPool>
{
    public ParticleEffectPool explosion;
    public ParticleEffectPool snowmanexplosion;

    @Override
    protected ParticleEffectPool load(FileRef basedir, Field field)
    {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(fieldToFileRef(field, basedir).getPath() + ".p"), Gdx.files.internal(
            "particles"));
        return new ParticleEffectPool(effect, 0, 250);
    }
}
