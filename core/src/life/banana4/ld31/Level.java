package life.banana4.ld31;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
    private int scoreValue = 0;
    private BitmapFont bitmapFont = new BitmapFont();

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
                e.kill();
            }
        }
        this.spawnQueue.add(e);
        e.onSpawn();
        return e;
    }

    public void render(DrawContext ctx, float delta)
    {
        multiplierDelta += delta;
        if (multiplierDelta >= 2f)
        {
            multiplier = 1;
        }
        int enemyCount = 0;
        for (Entity entity : this.entities)
        {
            if (entity instanceof Enemy)
            {
                enemyCount++;
            }
        }
        spawnEnemies(enemyCount);

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

    private int waveSpawn = 1;

    private void spawnEnemies(int curEnemyCount)
    {
        if (waveSpawn > 0)
        {
            do
            {
                float rX, rY;
                rX = (random.nextInt(width - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                rY = (random.nextInt(height - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                Vector2 playerV = new Vector2(this.player.getMidX(), this.player.getMidY());

                if (playerV.dst2(rX, rY) > 400 * 400)
                {
                    if (this.nodeAt(rX, rY).type != TileType.WALL)
                    {
                        addEntity(new PointEnemy().move(rX, rY));
                        waveSpawn--;
                        return;
                    }
                }
            }
            while (true);
        }
        if (curEnemyCount == 0)
        {
            waveSpawn = random.nextInt(enemiesKilled + 1) + enemiesKilled / 2;
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
        ctx.camera.setToOrtho(false);
        ctx.getSpriteBatch();
        spriteBatch.draw(this.game.getDrawContext().resources.textures.snowman, Gdx.graphics.getWidth() / 2 - 64,
                         Gdx.graphics.getHeight() / 2 - 64);
        bitmapFont.draw(spriteBatch, "Score: " + scoreValue + " Multiplier: " + multiplier + "x", 10, 20);
        ctx.camera.setToOrtho(true);
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

    private void detectCollisions()
    {
        List<Entity> recheck = new ArrayList<>();
        List<Entity> check = this.entities;

        int checkSize;
        final int entityCount = this.entities.size();
        do
        {
            recheck.clear();
            checkSize = check.size();
            for (int i = 0; i < checkSize; i++)
            {
                final Entity sourceEntities = check.get(i);
                if (sourceEntities.isDead() || !(sourceEntities instanceof CollisionSource))
                {
                    continue;
                }
                CollisionSource source = (CollisionSource)sourceEntities;
                for (int j = 0; j < entityCount; j++)
                {
                    final Entity targetEntity = this.entities.get(j);
                    if (sourceEntities == targetEntity || targetEntity.isDead()
                        || !(targetEntity instanceof CollisionTarget))
                    {
                        continue;
                    }
                    CollisionTarget target = (CollisionTarget)targetEntity;

                    if (!target.acceptsCollisionsFrom(source) || !source.mayCollideWith(target))
                    {
                        continue;
                    }

                    Vector2 rect = Collider.findCollision(sourceEntities, targetEntity);
                    if (rect != null)
                    {
                        float oldX = sourceEntities.getX();
                        float oldY = sourceEntities.getY();
                        source.onCollide(target, rect);
                        if (!sourceEntities.isDead() && (oldX != sourceEntities.getX()
                            || oldY != sourceEntities.getY()))
                        {
                            recheck.add(sourceEntities);
                        }

                        oldX = targetEntity.getX();
                        oldY = targetEntity.getY();
                        target.onCollide(source, rect);
                        if (!targetEntity.isDead() && (oldX != targetEntity.getX() || oldY != targetEntity.getY()))
                        {
                            recheck.add(targetEntity);
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

    private int multiplier = 1;
    private float multiplierDelta = 0;
    private int enemiesKilled = 0;

    public void addScore(int scoreValue)
    {
        multiplierDelta = 0;
        this.scoreValue += scoreValue * multiplier++;
        enemiesKilled++;
    }
}
