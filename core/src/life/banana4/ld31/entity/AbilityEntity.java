package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class AbilityEntity extends Entity implements CollisionTarget
{
    public AbilityEntity()
    {
        super(4, 4);
    }

    @Override
    public void onSpawn()
    {
        System.out.println("hi!");
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {

    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Filled);
        r.setColor(Color.YELLOW);
        r.circle(getX(), getY(), 3);
        r.end();
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        kill();
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Player;
    }
}
