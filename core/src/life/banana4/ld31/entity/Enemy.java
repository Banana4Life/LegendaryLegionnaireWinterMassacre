package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class Enemy extends LivingEntity
{
    public Enemy(float width, float height)
    {
        super(width, height);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
    }

    @Override
    public void onDeath()
    {
        getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.explosion)).move(
            getX(), getY());
        this.getLevel().addScore(getPoints());
    }

    public int getPoints()
    {
        return 25;
    }
}
