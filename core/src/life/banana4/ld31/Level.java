package life.banana4.ld31;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.ai.TiledManhattenDistance;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledRaycastCollisionDetector;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.collision.Collider;
import life.banana4.ld31.input.ControllerIntentionDetector;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.IntentionDetector;
import life.banana4.ld31.input.KeyboardIntentionDetector;

import static life.banana4.ld31.ai.TiledGraph.*;
import static life.banana4.ld31.input.IntentionDetector.NO_INTENTIONS;

public class Level
{
    private final List<Entity> entities;
    private final List<Entity> spawnQueue;
    private final List<Entity> removalQueue;
    private static final List<IntentionDetector> INTENTION_DETECTORS = new ArrayList<>();

    static
    {
        INTENTION_DETECTORS.add(new KeyboardIntentionDetector());
        INTENTION_DETECTORS.add(new ControllerIntentionDetector());
    }

    private final TiledGraph tiledGraph;
    private final TiledManhattenDistance heuristic = new TiledManhattenDistance();
    private final PathSmoother<TiledNode, Vector2> smoother;
    private final IndexedAStarPathFinder<TiledNode> pathFinder;


    public Level()
    {
        this.entities = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();

        this.tiledGraph = new TiledGraph().init(this);
        this.smoother = new PathSmoother<>(new TiledRaycastCollisionDetector(tiledGraph));
        this.pathFinder = new IndexedAStarPathFinder<>(tiledGraph);

        System.out.println("Running tests...");
        IndexedAStarPathFinder<TiledNode> pathFinder = new IndexedAStarPathFinder<>(tiledGraph);
        TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();

        pathFinder.searchNodePath(tiledGraph.getNode(12, 5), tiledGraph.getNode(16, 19), heuristic, path);

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

        addEntity(new Player()).move(100, 100);
    }

    void remove(Entity entity)
    {
        this.removalQueue.add(entity);
    }

    public <T extends Entity> T addEntity(T e)
    {
        e.setLevel(this);
        this.spawnQueue.add(e);
        return e;
    }

    public void render(DrawContext ctx, float delta)
    {
        drawLevel(ctx);
        // spawn queued
        this.entities.addAll(this.spawnQueue);
        this.spawnQueue.clear();

        Set<Intention> intentions = scanIntentions();

        // update living
        for (final Entity e : this.entities)
        {
            for (final Intention intention : intentions)
            {
                e.reactTo(intention, delta);
            }
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

    private int randomX1 = new Random().nextInt(TiledGraph.SIZE_X - 2) + 1;
    private int randomY1 = new Random().nextInt(TiledGraph.SIZE_Y - 2) + 1;
    private int randomX2 = new Random().nextInt(TiledGraph.SIZE_X - 2) + 1;
    private int randomY2 = new Random().nextInt(TiledGraph.SIZE_Y - 2) + 1;

    private void drawLevel(DrawContext ctx)
    {
        ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
        shapeRenderer.begin(ShapeType.Line);
        showGrid(shapeRenderer);
        showPath(shapeRenderer);
        shapeRenderer.end();
    }

    private void showGrid(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.CYAN);
        for (int height = 0; height < Gdx.graphics.getHeight(); height += TILE_SIZE)
        {
            shapeRenderer.line(0, height, 0, Gdx.graphics.getWidth(), height, 0);
        }
        for (int width = 0; width < Gdx.graphics.getWidth(); width += TILE_SIZE)
        {
            shapeRenderer.line(width, 0, 0, width, Gdx.graphics.getHeight(), 0);
        }
    }

    private void showPath(ShapeRenderer shapeRenderer)
    {
        TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();
        pathFinder.searchNodePath(tiledGraph.getNode(randomX1, randomY1), tiledGraph.getNode(randomX2, randomY2),
                                  heuristic, path);

        TiledNode last = path.get(0);
        for (TiledNode tiledNode : path)
        {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.box(tiledNode.getTileX() + TILE_SIZE_4, tiledNode.getTileY() + TILE_SIZE_4, 0, TILE_SIZE_2,
                              TILE_SIZE_2, 0);
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.line(last.getTileX() + TILE_SIZE_2, last.getTileY() + TILE_SIZE_2, 0,
                               tiledNode.getTileX() + TILE_SIZE_2, tiledNode.getTileY() + TILE_SIZE_2, 0);
            last = tiledNode;
        }

        smoother.smoothPath(path);
        last = path.get(0);
        for (TiledNode tiledNode : path)
        {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.box(tiledNode.getTileX() + TILE_SIZE_4, tiledNode.getTileY() + TILE_SIZE_4, 0, TILE_SIZE_2,
                              TILE_SIZE_2, 0);
            shapeRenderer.line(last.getTileX() + TILE_SIZE_2, last.getTileY() + TILE_SIZE_2, 0,
                               tiledNode.getTileX() + TILE_SIZE_2, tiledNode.getTileY() + TILE_SIZE_2, 0);
            last = tiledNode;
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

    private static Set<Intention> scanIntentions()
    {
        for (final IntentionDetector detector : INTENTION_DETECTORS)
        {
            Set<Intention> intentions = detector.detect();
            if (!intentions.isEmpty())
            {
                return intentions;
            }
        }
        return NO_INTENTIONS;
    }
}
