package life.banana4.ld31.entity;

public abstract class Enemy extends MovingEntity
{
    public Enemy(float width, float height)
    {
        super(width, height);
    }
    
    @Override
    public void onDeath()
    {
        getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.explosion)).move(
            getX(), getY());
    }
}
