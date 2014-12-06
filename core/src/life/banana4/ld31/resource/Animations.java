package life.banana4.ld31.resource;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

import java.lang.reflect.Field;

public class Animations extends ResourceBag<Animation> {
    public Animations legs;

    @Override
    protected Animation load(FileRef basedir, Field field) {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".png").getPath());
        return new Animation(0.1f, new TextureRegion(new Texture(basedir.child(id + ".png").getPath())));
    }
}
