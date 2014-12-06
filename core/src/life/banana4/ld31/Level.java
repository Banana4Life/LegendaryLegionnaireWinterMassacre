package life.banana4.ld31;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.badlogic.gdx.math.Rectangle;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.entity.collision.Collider;

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

    private static long pair(Entity a, Entity b)
    {
        return (((long)a.id) << 32) | b.id;
    }

    private void detectCollisions()
    {
        final Set<Long> checked = new HashSet<>();

        for (Entity a : this.entities)
        {
            for (Entity b : this.entities)
            {
                if (a == b)
                {
                    continue;
                }
                final long id = pair(a, b);
                if (checked.contains(id))
                {
                    continue;
                }
                checked.add(id);
                checked.add(pair(b, a));

                Rectangle rect = Collider.findCollision(a, b);
                if (rect != null)
                {
                    a.onCollide(b, rect);
                    b.onCollide(a, rect);
                }
            }
        }
    }
}
