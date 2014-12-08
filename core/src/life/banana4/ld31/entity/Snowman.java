package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.ShipRocket;

public class Snowman extends LivingEntity implements CollisionTarget
{
    public Snowman()
    {
        super(64, 64);
        setPosition(492, 467);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        final SpriteBatch spriteBatch = ctx.getSpriteBatch();
        spriteBatch.begin();
        spriteBatch.draw(ctx.resources.textures.snowman, getX() - 32, getY() - 32);
        spriteBatch.end();
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        getLevel().addEntity(new Particle(
            getLevel().getGame().getDrawContext().resources.particles.snowmanexplosion).move(getMidX(), getMidY()));
        getLevel().addEntity(new Crater(getX(), getY()));
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        kill();
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof ShipRocket;
    }
}
