package life.banana4.ld31.resource;

import java.lang.reflect.Field;
import com.badlogic.gdx.graphics.Texture;
import life.banana4.util.resourcebags.FileRef;
import life.banana4.util.resourcebags.ResourceBag;

public class Textures extends ResourceBag<Texture>
{
    public Texture floor;
    public Texture bolt;
    public Texture snowman;
    public Texture snowmanexplosion;
    public Texture alienprojectile;
    public Texture shipprojectile;
    public Texture pickup1;
    public Texture pickup2;
    public Texture cursor;
    public Texture footstep;
    public Texture ship;
    public Texture uibar;
    public Texture healthbar;
    public Texture staminabar;

    @Override
    protected Texture load(FileRef basedir, Field field)
    {
        final String id = field.getName();
        System.out.println(basedir.child(id + ".png").getPath());
        return new Texture(basedir.child(id + ".png").getPath());
    }
}
