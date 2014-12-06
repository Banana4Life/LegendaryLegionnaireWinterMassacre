package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class Projectile extends MovingEntity implements CollisionSource
{
    private final Entity shooter;

    public Projectile(Entity shooter, float width, float height)
    {
        super(width, height);
        this.shooter = shooter;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
    }

    @Override
    public void draw(DrawContext ctx)
    {
        ShapeRenderer r = ctx.getShapeRenderer();

        r.begin(ShapeType.Filled);
        r.circle(getX(), getY(), 3);
        r.end();
    }

    @Override
    public void onCollide(Rectangle rect, CollisionTarget target)
    {
        System.out.println("projectile meets " + target.getClass().getSimpleName());
        if (target instanceof Wall)
        {
            die();
        }
        if (shooter instanceof Player && target instanceof Enemy)
        {
            ((Enemy)target).die();
            die();
        }
    }
}
