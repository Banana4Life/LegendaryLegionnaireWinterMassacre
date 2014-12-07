package life.banana4.ld31;

import com.badlogic.gdx.graphics.OrthographicCamera;
import life.banana4.ld31.resource.Animations;
import life.banana4.ld31.resource.Levels;
import life.banana4.ld31.resource.Particles;
import life.banana4.ld31.resource.Sounds;
import life.banana4.ld31.resource.Textures;
import life.banana4.util.resourcebags.Resources;

public class Ld31Resources extends Resources
{
    public Textures textures;
    public Animations animations;
    public Sounds sounds;
    public Levels levels;
    public Particles particles;

    public Ld31Resources(Ld31 ld31, OrthographicCamera camera)
    {
        textures = new Textures();
        animations = new Animations();
        sounds = new Sounds();
        levels = new Levels(ld31, camera, textures);
        particles = new Particles();
    }
}
