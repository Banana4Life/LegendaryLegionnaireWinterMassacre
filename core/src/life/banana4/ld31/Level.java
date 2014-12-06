package life.banana4.ld31;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.ai.TiledManhattenDistance;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledRaycastCollisionDetector;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.collision.Collider;

public class Level
{
    private final List<Entity> entities;
    private final List<Entity> spawnQueue;
    private final List<Entity> removalQueue;

    private final TiledGraph tiledGraph;
    private final TiledManhattenDistance heuristic = new TiledManhattenDistance();
    private final PathSmoother<TiledNode, Vector2> smoother;


    public Level()
    {
        this.entities = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();

        tiledGraph = new TiledGraph().init();
        smoother = new PathSmoother<>(new TiledRaycastCollisionDetector(tiledGraph));

        System.out.println("Running tests...");
        IndexedAStarPathFinder<TiledNode> pathFinder = new IndexedAStarPathFinder<>(tiledGraph);
        TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();

        pathFinder.searchNodePath(tiledGraph.getNode(12, 5), tiledGraph.getNode(16,19), heuristic, path);

        for (TiledNode tiledNode : path)
        {
            System.out.println(tiledNode.x + ":" + tiledNode.y);
        }

        smoother.smoothPath(path);
        System.out.println("smoothed");
        for (TiledNode tiledNode : path)
        {
            System.out.println(tiledNode.x + ":" + tiledNode.y);
        }
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