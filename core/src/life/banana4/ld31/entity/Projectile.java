package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;

public class Projectile extends MovingEntity
{
    public Projectile(float width, float height)
    {
        super(width, height);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
    }

    @Override
    public void draw(DrawContext ctx)
    {
        ShapeRenderer r = ctx.getShapeRenderer();

        r.begin(ShapeType.Filled);
        r.circle(getX(), getY(), 3);
        r.end();
    }
}
