package life.banana4.ld31;

import java.util.ArrayList;
import java.util.List;
import life.banana4.ld31.ai.TiledGraph;

public class Level
{
    private final List<Entity> entities;
    private final List<Entity> spawnQueue;
    private final List<Entity> removalQueue;

    public Level()
    {
        this.entities = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();
        new TiledGraph().init();
    }

    void remove(Entity entity)
    {
        this.removalQueue.add(entity);
    }

    public void addEntity(Entity e)
    {
        e.setLevel(this);
        this.spawnQueue.add(e);
    }

    public void render(DrawContext ctx, float delta)
    {
        // spawn queued
        this.entities.addAll(this.spawnQueue);
        this.spawnQueue.clear();

        // update living
        for (final Entity e : this.entities)
        {
            e.update(delta);
        }

        // remove dead
        this.entities.removeAll(this.removalQueue);
        this.removalQueue.clear();

        // draw living
        for (final Entity e : this.entities)
        {
            e.draw(ctx);
        }
    }

    private void detectCollitions()
    {

    }
}
