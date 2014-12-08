package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class FireProjectile extends Projectile
{
    private final PooledEffect effect;
    private final ParticleEffectPool effectPool;

    public FireProjectile(Entity shooter, float width, float height, ParticleEffectPool effectPool)
    {
        super(shooter, 250, width, height);
        this.effectPool = effectPool;
        this.effect = effectPool.obtain();
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        effect.start();
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);

        this.effect.setPosition(getX(), getY());
        if (effect.isComplete())
        {
            kill();
            effectPool.free(effect);
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch b = ctx.getSpriteBatch();
        b.begin();
        this.effect.draw(b, delta);
        b.end();
    }

    @Override
    public float getSpeed()
    {
        return 400;
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return !(target instanceof AlienShip) && super.mayCollideWith(target);
    }
}
