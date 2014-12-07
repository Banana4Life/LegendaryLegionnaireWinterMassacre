package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;

public class Particle extends Entity
{
    private final PooledEffect effect;

    public Particle(PooledEffect effect)
    {
        super(0, 0);
        this.effect = effect;
    }

    @Override
    public void onSpawn()
    {
        effect.start();
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        this.effect.setPosition(getX(), getY());
        if (effect.isComplete())
        {
            kill();
            this.getLevel().getGame().getDrawContext().resources.particles.explosionPool.free(effect);
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
}
