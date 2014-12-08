package life.banana4.ld31.entity.pickup;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.LivingEntity;
import life.banana4.ld31.entity.Particle;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public abstract class Pickup extends LivingEntity implements CollisionTarget
{
    protected Texture texture;

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
        b.draw(texture, this.getX(), this.getY());
        b.end();
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Player;
    }
}
