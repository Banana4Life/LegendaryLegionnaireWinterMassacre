package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.ai.TiledGraph;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;

public class PointEnemy extends Enemy
{
    private static float SPEED = 75;

    private TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();

    public PointEnemy()
    {
        super(5, 5);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (path.getCount() < 2)
        {
            return;
        }
        TiledNode tiledNode = path.get(1);
        float x = this.getX() / TiledGraph.TILE_SIZE;
        float y = this.getY() / TiledGraph.TILE_SIZE;
        x = tiledNode.x - x;
        y = tiledNode.y - y;
        double length = Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        this.setVelocity(x * SPEED,y * SPEED);
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
