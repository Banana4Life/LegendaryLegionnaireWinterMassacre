package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class Pickup extends LivingEntity implements CollisionTarget
{
    public Pickup()
    {
        super(31, 31);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        if (lifetime() > 10000)
        {
            kill();
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        SpriteBatch b = ctx.getSpriteBatch();
        b.begin();
        b.draw(this.getLevel().getGame().getDrawContext().resources.textures.pickup1, this.getX(), this.getY());
        b.end();
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Player)
        {
            ((Player)source).heal(25);
            getLevel().addEntity(new Particle(
                getLevel().getGame().getDrawContext().resources.particles.heal).move(((Player)source).getMidX(), ((Player)source).getMidY()));
        }
        kill();
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Player;
    }
}
