package life.banana4.ld31;

import life.banana4.ld31.resource.Animations;
import life.banana4.ld31.resource.Levels;
import life.banana4.ld31.resource.Sounds;
import life.banana4.ld31.resource.Textures;
import life.banana4.ld31.resource.Tilemaps;
import life.banana4.util.resourcebags.Resources;

public class Ld31Resources extends Resources
{
    public Textures textures;
    public Animations animations;
    public Tilemaps tilemaps;
    public Sounds sounds;
    public Levels levels;

    public Ld31Resources()
    {
        textures = new Textures();
        animations = new Animations();
        tilemaps = new Tilemaps();
        sounds = new Sounds();
        levels = new Levels();
    }
}
