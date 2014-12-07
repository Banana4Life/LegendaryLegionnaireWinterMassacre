package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class Enemy extends MovingEntity
{
    private int scoreValue = 100;

    protected int health = 1;

    public Enemy(float width, float height)
    {
        super(width, height);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        if (health <= 0)
        {
            this.die();
        }
    }

    @Override
    public void onDeath()
    {
        getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.explosion)).move(
            getX(), getY());
        this.getLevel().addScore(this.scoreValue);
    }
}
