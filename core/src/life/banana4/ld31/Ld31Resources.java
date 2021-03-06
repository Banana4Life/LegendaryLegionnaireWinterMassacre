package life.banana4.ld31;

import life.banana4.ld31.resource.Animations;
import life.banana4.ld31.resource.LevelLoader;
import life.banana4.ld31.resource.Particles;
import life.banana4.ld31.resource.Songs;
import life.banana4.ld31.resource.Sounds;
import life.banana4.ld31.resource.Textures;
import life.banana4.util.resourcebags.Resources;

public class Ld31Resources extends Resources
{
    public Textures textures;
    public Animations animations;
    public Sounds sounds;
    public Songs songs;
    public Particles particles;

    public Ld31Resources()
    {
        textures = new Textures();
        animations = new Animations();
        sounds = new Sounds();
        songs = new Songs();
        particles = new Particles();
        particles = new Particles();
    }
}
