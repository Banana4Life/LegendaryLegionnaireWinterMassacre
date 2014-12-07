package life.banana4.ld31.entity;

public abstract class Enemy extends MovingEntity
{
    private int scoreValue = 100;

    public Enemy(float width, float height)
    {
        super(width, height);
    }
    
    @Override
    public void onDeath()
    {
        getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.explosion)).move(
            getX(), getY());
        this.getLevel().addScore(this.scoreValue);
    }
}
