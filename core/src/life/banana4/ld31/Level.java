package life.banana4.ld31;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.ai.TiledManhattenDistance;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledRaycastCollisionDetector;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.Enemy;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.PointEnemy;
import life.banana4.ld31.entity.collision.Collider;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.input.ControllerIntentionDetector;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.IntentionDetector;
import life.banana4.ld31.input.KeyboardIntentionDetector;
import life.banana4.ld31.util.TileType;

import static life.banana4.ld31.input.IntentionDetector.NO_INTENTIONS;
import static life.banana4.ld31.resource.Levels.TILE_WIDTH;
import static life.banana4.ld31.resource.Levels.TILE_WIDTH_2;

public class Level
{
    private final List<Entity> entities;
    private final List<Entity> spawnQueue;
    private final List<Entity> removalQueue;
    private final List<FloorTile> floor;
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
    private final Player player;

    private final int width;
    private final int height;

    Ld31 game;

    private boolean debug = true; // TODO debug
    private Random random = new Random();

    public Level(int width, int height, TiledGraph tiledGraph)
    {
        this.width = width;
        this.height = height;

        this.entities = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();
        this.floor = new ArrayList<>();

        this.tiledGraph = tiledGraph;
        this.smoother = new PathSmoother<>(new TiledRaycastCollisionDetector(tiledGraph));
        this.pathFinder = new IndexedAStarPathFinder<>(tiledGraph);

        player = new Player();
        addEntity(player).move(550, 300);
    }

    public Player getPlayer()
    {
        return this.player;
    }

    void remove(Entity entity)
    {
        this.removalQueue.add(entity);
    }

    public <T extends Entity> T addEntity(T e)
    {
        e.setLevel(this);
        if (e instanceof Enemy)
        {
            if (this.nodeAt(e.getX(), e.getY()).type == TileType.WALL)
            {
                e.die();
            }
        }
        this.spawnQueue.add(e);
        e.onSpawn();
        return e;
    }

    public void render(DrawContext ctx, float delta)
    {
        int enemyCount = 0;
        for (Entity entity : this.entities)
        {
            if (entity instanceof Enemy)
            {
                enemyCount++;
            }
        }
        if (enemyCount < 4)
        {
            float rX, rY;
            int spawns = 0;
            Vector2 playerV = new Vector2(this.player.getMidX(), this.player.getMidY());
            do
            {
                rX = (random.nextInt(width - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                rY = (random.nextInt(height - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                if (playerV.dst2(rX, rY) > 200 * 200)
                {
                    addEntity(new PointEnemy().move(rX, rY));
                    spawns++;
                }
            }
            while (spawns < 9);
        }

        drawLevel(ctx);
        // spawn queued
        this.entities.addAll(this.spawnQueue);
        this.spawnQueue.clear();

        Set<Intention> intentions = scanIntentions();

        // update living
        for (int i = 0; i < this.entities.size(); i++)
        {
            final Entity e = this.entities.get(i);
            for (final Intention intention : intentions)
            {
                e.reactTo(intention, delta);
            }
            e.update(ctx.camera, delta);
        }

        detectCollisions();

        // remove dead
        this.entities.removeAll(this.removalQueue);
        this.removalQueue.clear();

        // draw living
        for (final Entity e : this.entities)
        {
            e.draw(ctx, delta);
        }
    }

    private void drawLevel(DrawContext ctx)
    {
        //draw floor
        final SpriteBatch spriteBatch = ctx.getSpriteBatch();
        spriteBatch.begin();
        for (final FloorTile t : this.floor)
        {
            t.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private void showGrid(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.CYAN);
        for (int height = 0; height < Gdx.graphics.getHeight(); height += TILE_WIDTH)
        {
            shapeRenderer.line(0, height, 0, Gdx.graphics.getWidth(), height, 0);
        }
        for (int width = 0; width < Gdx.graphics.getWidth(); width += TILE_WIDTH)
        {
            shapeRenderer.line(width, 0, 0, width, Gdx.graphics.getHeight(), 0);
        }
    }

    public void calculatePath(TiledSmoothableGraphPath path, Entity entity)
    {
        path.clear();
        if (entity instanceof Enemy)
        {
            pathFinder.searchNodePath(tiledGraph.getNode((int)(entity.getMidX() / TILE_WIDTH),
                                                         (int)(entity.getMidY() / TILE_WIDTH)), tiledGraph.getNode(
                (int)(player.getMidX() / TILE_WIDTH), (int)(player.getMidY() / TILE_WIDTH)), heuristic, path);
            smoother.smoothPath(path);
        }
    }

    private static long pair(Entity a, Entity b)
    {
        return (((long)a.id) << 32) | b.id;
    }

    private static final TLongSet CHECKED_COLLISIONS = new TLongHashSet();

    private void detectCollisions()
    {
        List<Entity> recheck = new ArrayList<>();
        List<Entity> check = this.entities;
        do
        {
            CHECKED_COLLISIONS.clear();
            recheck.clear();
            for (int i = 0; i < check.size(); i++)
            {
                final Entity a = check.get(i);
                if (a.isDead() || !(a instanceof CollisionSource))
                {
                    continue;
                }
                CollisionSource source = (CollisionSource)a;
                for (int j = 0; j < this.entities.size(); j++)
                {
                    final Entity b = this.entities.get(j);
                    if (a == b || b.isDead() || !(b instanceof CollisionTarget))
                    {
                        continue;
                    }
                    CollisionTarget target = (CollisionTarget)b;

                    final long id = pair(a, b);
                    if (CHECKED_COLLISIONS.contains(id))
                    {
                        continue;
                    }
                    CHECKED_COLLISIONS.add(id);
                    CHECKED_COLLISIONS.add(pair(b, a));

                    Vector2 rect = Collider.findCollision(a, b);
                    if (rect != null)
                    {
                        float oldX = a.getX();
                        float oldY = a.getY();
                        source.onCollide(target, rect);
                        if (!a.isDead() && (oldX != a.getX() || oldY != a.getY()))
                        {
                            recheck.add(a);
                        }

                        oldX = b.getX();
                        oldY = b.getY();
                        target.onCollide(source, rect);
                        if (!b.isDead() && (oldX != b.getX() || oldY != b.getY()))
                        {
                            recheck.add(b);
                        }
                    }
                }
            }
            check = recheck;
        }
        while (!recheck.isEmpty());
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

    public List<FloorTile> getFloor()
    {
        return floor;
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

    public TiledNode nodeAt(float x, float y)
    {
        return this.tiledGraph.getNode((int)(x / TILE_WIDTH), (int)(y / TILE_WIDTH));
    }

    public boolean isDebug()
    {
        return debug;
    }

    public Ld31 getGame()
    {
        return this.game;
    }

    public Random getRandom()
    {
        return random;
    }
}
