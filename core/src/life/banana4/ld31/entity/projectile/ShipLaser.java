package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.Projectile;
import life.banana4.ld31.entity.Snowman;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class ShipLaser extends Projectile implements CollisionSource
{
    public ShipLaser(AlienShip ship)
    {
        super(ship, 10, 10);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Filled);
        r.setColor(Color.RED);
        r.rect(getX(), getY(), 10, 10);
        r.end();
    }

    @Override
    public float getSpeed()
    {
        return 1000;
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return target instanceof Snowman;
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Snowman)
        {
            kill();
        }
    }
}
