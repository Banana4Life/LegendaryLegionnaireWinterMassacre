package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;

import static life.banana4.ld31.resource.Levels.TILE_WIDTH;

public class PointEnemy extends Enemy
{
    private static final float SPEED = 75;
    private static final float ATTACK_RANGE = 300;
    private static final float SHOT_DELAY = 0.15f;

    private TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();
    private float waitedFor = 0;

    public PointEnemy()
    {
        super(5, 5);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        if (path.getCount() < 2)
        {
            return;
        }
        TiledNode tiledNode = path.get(1);
        float x = this.getX() / TILE_WIDTH;
        float y = this.getY() / TILE_WIDTH;
        x = tiledNode.x - x;
        y = tiledNode.y - y;
        double length = Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        this.setVelocity(x * SPEED, y * SPEED);

        Player p = getLevel().getPlayer();

        float dx = p.getX() - getX();
        float dy = p.getY() - getY();
        waitedFor += delta;
        if (dx * dx + dy * dy < ATTACK_RANGE * ATTACK_RANGE && waitedFor > SHOT_DELAY)
        {
            shoot(new Projectile(3, 3), dx, dy, 200);
            waitedFor = 0;
        }
    }

    @Override
    public void draw(DrawContext ctx)
    {
        ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.circle(this.getX(), this.getY(), 10);

        this.getLevel().calculatePath(this.path, shapeRenderer, this);
        shapeRenderer.end();
    }
}
