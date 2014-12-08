package life.banana4.ld31;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.ai.TiledManhattenDistance;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledRaycastCollisionDetector;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.pickup.HealthPickup;
import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.BossEnemy;
import life.banana4.ld31.entity.Cursor;
import life.banana4.ld31.entity.Enemy;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.PointEnemy;
import life.banana4.ld31.entity.Snowman;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.pickup.ScrollPickup;
import life.banana4.ld31.entity.projectile.Bolt;
import life.banana4.ld31.input.ControllerIntentionDetector;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;
import life.banana4.ld31.input.IntentionDetector;
import life.banana4.ld31.input.KeyboardIntentionDetector;
import life.banana4.ld31.util.TileType;

import static java.lang.Math.abs;
import static life.banana4.ld31.input.IntentionDetector.NO_INTENTIONS;
import static life.banana4.ld31.resource.LevelLoader.TILE_WIDTH;
import static life.banana4.ld31.resource.LevelLoader.TILE_WIDTH_2;

public class Level
{
    private static final int MAX_WAVE = 2;
    private static final DepthComparator BY_DEPTH_ORDER = new DepthComparator();
    public static final int SPAWN_DISTANCE = 400;
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
    private final Cursor cursor;
    private final AlienShip alienShip;
    private final Snowman snowman;
    private Player player;

    private final int width;
    private final int height;

    private final Ld31 game;

    private Random random = new Random();
    private int scoreValue = 0;
    private BitmapFont bitmapFont = new BitmapFont();
    private final OrthographicCamera uiCamera = new OrthographicCamera();

    public Level(Ld31 game, int width, int height, TiledGraph tiledGraph)
    {
        this.game = game;
        this.width = width;
        this.height = height;

        this.entities = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();
        this.floor = new ArrayList<>();

        this.tiledGraph = tiledGraph;
        this.smoother = new PathSmoother<>(new TiledRaycastCollisionDetector(tiledGraph));
        this.pathFinder = new IndexedAStarPathFinder<>(tiledGraph);
        this.uiCamera.setToOrtho(false);

        spawnPlayer();
        this.snowman = addEntity(new Snowman());
        this.cursor = addEntity(new Cursor());
        this.alienShip = addEntity(new AlienShip(snowman));
        this.cursor.move(snowman.getMidX(), snowman.getMidY());
    }

    private void spawnPlayer()
    {
        player = new Player();
        addEntity(player).move(800, 90);
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
        if (!this.player.isDead() && alienShip.hasPassed())
        {
            spawnEnemies(enemyCount);
            spawnAbility(delta);
        }

        if (waveNumber == MAX_WAVE)
        {
            this.alienShip.startEndFight();
        }

        draw(ctx);
        // spawn queued
        this.entities.addAll(this.spawnQueue);
        this.spawnQueue.clear();
        Collections.sort(this.entities, BY_DEPTH_ORDER);

        Set<Intention> intentions = scanIntentions();
        filterIntentions(intentions);

        for (Intention intention : intentions)
        {
            this.reactTo(intention, delta);
        }

        // update living
        for (int i = 0; i < this.entities.size(); i++)
        {
            final Entity e = this.entities.get(i);
            for (final Intention intention : intentions)
            {
                e.reactTo(intention, delta);
            }
            e.update(ctx.camera, delta);
            e.checkInWorld();
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

    private void filterIntentions(Set<Intention> intentions)
    {
        if (!alienShip.hasPassed())
        {
            for (Iterator<Intention> it = intentions.iterator(); it.hasNext(); )
            {
                if (it.next().getType() != Type.EXIT_GAME)
                {
                    it.remove();
                }
            }
        }
    }

    private void reactTo(Intention intention, float delta)
    {
        switch (intention.getType())
        {
            case EXIT_GAME:
                Gdx.app.exit();
                break;
            case NEW_GAME:
                this.game.reset();
        }
    }

    private float waited;

    private void spawnAbility(float delta)
    {
        final float ABILITY_DELAY = 10f;

        waited += delta;
        if (waited < ABILITY_DELAY)
        {
            return;
        }
        waited = 0;

        List<TiledNode> nodes = this.tiledGraph.getWalkableNodes();
        TiledNode target = nodes.get(random.nextInt(nodes.size()));
        addEntity(new HealthPickup()).move(target.x * TILE_WIDTH, target.y * TILE_WIDTH);

        if (random.nextInt(10) == 0)
        {
            target = nodes.get(random.nextInt(nodes.size()));
            addEntity(new ScrollPickup().move(target.x * TILE_WIDTH, target.y * TILE_WIDTH));
        }
    }

    private int waveSpawn = 1;
    private int waveNumber = 0;

    private void spawnEnemies(int curEnemyCount)
    {
        if (waveNumber == MAX_WAVE)
        {
            return;
        }
        if (waveSpawn > 0 && curEnemyCount < 250)
        {
            Enemy enemy;
            int bossChance = MAX_WAVE - waveNumber;
            if (bossChance <= 1)
            {
                bossChance = 1;
            }
            if (random.nextInt(bossChance) == 0)
            {
                enemy = new BossEnemy();
            }
            else
            {
                enemy = new PointEnemy();
            }
            do
            {
                float rX, rY;
                rX = (random.nextInt(width - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                rY = (random.nextInt(height - 2) + 1) * TILE_WIDTH + TILE_WIDTH_2;
                Vector2 playerV = new Vector2(this.player.getMidX(), this.player.getMidY());

                if (playerV.dst2(rX, rY) > SPAWN_DISTANCE * SPAWN_DISTANCE)
                {
                    if (this.nodeAt(rX, rY).type != TileType.WALL
                     && this.nodeAt(rX + enemy.getWidth(), rY + enemy.getHeight()).type != TileType.WALL
                     && this.nodeAt(rX + enemy.getWidth(), rY).type != TileType.WALL
                     && this.nodeAt(rX, rY + enemy.getHeight()).type != TileType.WALL)
                    {
                        this.addEntity(enemy.move(rX, rY));
                        waveSpawn--;
                        return;
                    }
                }
            }
            while (true);
        }
        if (curEnemyCount == 0)
        {
            waveSpawn = 1 + random.nextInt(waveNumber + 1) + waveNumber / 2;
            waveNumber++;
        }
    }

    private void draw(DrawContext ctx)
    {
        //draw floor
        final SpriteBatch spriteBatch = ctx.getSpriteBatch();
        spriteBatch.begin();
        for (final FloorTile t : this.floor)
        {
            t.draw(spriteBatch);
        }

        spriteBatch.draw(ctx.resources.textures.uibar, 0, Gdx.graphics.getHeight() - 48, 1280, 48, 0, 0, 1280, 48, false, true);
        spriteBatch.draw(ctx.resources.textures.healthbar, 8, Gdx.graphics.getHeight() - 40, 87 * player.getHealth() / player.getMaxHealth(), 13, 0, 0, 1, 13, false, true);
        spriteBatch.draw(ctx.resources.textures.staminabar, 8, Gdx.graphics.getHeight() - 21, 87 * Math.max(1 - player.getStamina(), 0), 13, 0, 0, 1, 13, false, true);

        spriteBatch.setProjectionMatrix(this.uiCamera.combined);
        bitmapFont.draw(spriteBatch, "" + scoreValue, 480, 38);
        bitmapFont.draw(spriteBatch, multiplier + "x", 805, 38);
        bitmapFont.draw(spriteBatch, waveCount + "/50", 805, 22);

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
        if (entity instanceof Enemy && !player.isDead())
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
                    Vector2 rect = findCollision(sourceEntities, targetEntity);
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

    private static final Vector2 amin = new Vector2(0, 0);
    private static final Vector2 amax = new Vector2(0, 0);
    private static final Vector2 bmin = new Vector2(0, 0);
    private static final Vector2 bmax = new Vector2(0, 0);

    private static final Vector2 mtv = new Vector2(0, 0);

    public static Vector2 findCollision(Entity a, Entity b)
    {
        amin.set(a.getX(), a.getY());
        amax.set(a.getX() + a.getWidth(), a.getY() + a.getHeight());
        bmin.set(b.getX(), b.getY());
        bmax.set(b.getX() + b.getWidth(), b.getY() + b.getHeight());

        mtv.set(0, 0);

        float left = bmin.x - amax.x;
        float right = bmax.x - amin.x;
        float top = bmin.y - amax.y;
        float bottom = bmax.y - amin.y;

        if (left > 0 || right < 0 || top > 0 || bottom < 0)
        {
            return null;
        }
        else
        {
            mtv.x = abs(left) < right ? left : right;
            mtv.y = abs(top) < bottom ? top : bottom;
            if (abs(mtv.x) < abs(mtv.y))
            {
                mtv.y = 0;
            }
            else
            {
                mtv.x = 0;
            }
            if (mtv.x == 0 && mtv.y == 0)
            {
                return null;
            }
            return mtv;
        }
    }

    private Set<Intention> scanIntentions()
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

    public Cursor getCursor()
    {
        return cursor;
    }

    public void shutdown()
    {
        for (final Entity entity : entities)
        {
            entity.kill();
        }
    }

    private static final class DepthComparator implements Comparator<Entity>
    {
        @Override
        public int compare(Entity a, Entity b)
        {
            int delta = a.getDepth() - b.getDepth();
            if (delta == 0)
            {
                return 0;
            }
            if (delta > 0)
            {
                return 1;
            }
            return -1;
        }
    }
}
