package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Animations extends ResourceBag<Animation>
{
    public Animation legs;
    public Animation charcrossload;
    public Animation charswordswing;

    @Override
    protected Animation load(FileRef basedir, Field field)
    {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".png").getPath());

        Texture tmpTexture = new Texture(basedir.child(id + ".png").getPath());
        TextureRegion[][] tmp = TextureRegion.split(tmpTexture, tmpTexture.getWidth(), tmpTexture.getWidth());
        TextureRegion[] frames = new TextureRegion[tmpTexture.getHeight() / tmpTexture.getWidth()];

        for (int i = 0; i < frames.length; i++)
        {
            frames[i] = tmp[i][0];
        }

        float speed;
        switch (id)
        {
            case "charswordswing":
                speed = 0.05f;
                break;
            case "legs":
                speed = 0.05f;
                break;
            default:
                speed = 0.1f;
        }

        return new Animation(speed, frames);
    }
}
