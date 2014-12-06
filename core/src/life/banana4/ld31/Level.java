package life.banana4.ld31;

import java.util.ArrayList;
import java.util.List;

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
    }

    void remove(Entity entity)
    {
        this.removalQueue.add(entity);
    }

    public void addEntity(Entity e) {
        e.setLevel(this);
        this.spawnQueue.add(e);
    }

    public void update(float delta) {
        // spawn queued
        this.entities.addAll(this.spawnQueue);
        this.spawnQueue.clear();

        // update living
        for (final Entity e : this.entities) {
            e.update(delta);
        }

        // remove dead
        this.entities.removeAll(this.removalQueue);
        this.removalQueue.clear();

        // draw living
        for (final Entity e : this.entities) {
            e.draw(delta);
        }
    }
}
