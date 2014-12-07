package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;

public class Particle extends Entity
{
    private final ParticleEffect effect;

    public Particle(ParticleEffect effect)
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
        if (effect.isComplete())
        {
            die();
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        this.effect.draw(ctx.getSpriteBatch(), delta);
    }
}
