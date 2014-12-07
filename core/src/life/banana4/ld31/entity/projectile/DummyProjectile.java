package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.Projectile;

public class DummyProjectile extends Projectile
{
    public DummyProjectile(Entity shooter, float width, float height)
    {
        super(shooter, width, height);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);


        ShapeRenderer r = ctx.getShapeRenderer();

        r.begin(ShapeType.Filled);
        if (getShooter() instanceof Player)
        {
            r.setColor(Color.BLUE);
            r.circle(getMidX(), getMidY(), getWidth());
        }
        else
        {
            r.setColor(Color.RED);
            r.circle(getMidX(), getMidY(), 3);
        }

        r.end();
    }

    @Override
    public float getSpeed()
    {
        return 400;
    }
}
