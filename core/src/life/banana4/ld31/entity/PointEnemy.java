package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

import static life.banana4.ld31.resource.Levels.TILE_WIDTH;
import static life.banana4.ld31.resource.Levels.TILE_WIDTH_2;

public class PointEnemy extends Enemy implements CollisionSource, CollisionTarget
{
    private static final float SPEED = 120;
    private static final float ATTACK_RANGE = 300;
    private static final float SHOT_DELAY = 0.65f;

    private TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();
    private float waitedFor = 0;

    public PointEnemy()
    {
        super(20, 20);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        this.getLevel().calculatePath(path, this);
        if (path.getCount() == 0)
        {
            this.setVelocity(0, 0);
            return;
        }

        float x;
        float y;
        if (path.getCount() <= 2)
        {
            x = this.getLevel().getPlayer().getMidX() - this.getMidX();
            y = this.getLevel().getPlayer().getMidY() - this.getMidY();
        }
        else
        {
            TiledNode tiledNode = path.get(1);
            x = tiledNode.x * TILE_WIDTH + TILE_WIDTH_2 - this.getMidX();
            y = tiledNode.y * TILE_WIDTH + TILE_WIDTH_2 - this.getMidY();
        }
        double length = Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        this.setVelocity(x * SPEED, y * SPEED);

        Player p = getLevel().getPlayer();

        float dx = p.getX() - getX();
        float dy = p.getY() - getY();
        waitedFor += delta;
        if (dx * dx + dy * dy < ATTACK_RANGE * ATTACK_RANGE && waitedFor > SHOT_DELAY && path.getCount() == 2)
        {
            shoot(new Projectile(this, 3, 3), dx, dy, 200);
            waitedFor = 0;
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.circle(this.getMidX(), this.getMidY(), 10);

        /*
        if (this.path.getCount() > 1)
        {
            TiledNode last = path.get(0);
            for (TiledNode tiledNode : path)
            {
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.line(last.getTileX() + TILE_WIDTH_2, last.getTileY() + TILE_WIDTH_2, 0,
                                   tiledNode.getTileX() + TILE_WIDTH_2, tiledNode.getTileY() + TILE_WIDTH_2, 0);
                last = tiledNode;
            }
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.box(last.getTileX() + TILE_WIDTH_4, last.getTileY() + TILE_WIDTH_4, 0, TILE_WIDTH_2,
                              TILE_WIDTH_2, 0);
        }
        */
        shapeRenderer.end();
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {

    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {

    }
}
