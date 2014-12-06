package life.banana4.ld31.entity;

public abstract class Enemy extends MovingEntity
{
    public Enemy(float width, float height)
    {
        super(width, height);
    }

    public Projectile shoot(Projectile p, float x, float y)
    {
        getLevel().addEntity(p);
        p.move(getX(), getY());
        p.setVelocity(x, y);
        return p;
    }

    public Projectile shoot(Projectile p, float x, float y, float speed)
    {
        float len = (float)Math.sqrt(x * x + y * y);
        return shoot(p, (x / len) * speed, (y / len) * speed);
    }
}
