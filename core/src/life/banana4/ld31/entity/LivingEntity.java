package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.Entity;

public abstract class LivingEntity extends Entity
{
    protected float vx;
    protected float vy;

    private int health = 1;

    public LivingEntity(float width, float height)
    {
        super(width, height);
    }

    public void accelerate(float x, float y)
    {
        this.vx += x;
        this.vy += y;
    }

    public void setVelocity(float vx, float vy)
    {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        this.move(vx * delta, vy * delta);
        if (getHealth() <= 0)
        {
            this.kill();
        }
    }

    public Projectile shoot(Projectile p, float x, float y)
    {
        getLevel().addEntity(p);
        p.move(getMidX(), getMidY());
        p.setVelocity(x, y);
        p.setRotation(new Vector2(x, y).angle());
        return p;
    }

    public Projectile shoot(Projectile p, float x, float y, float speed)
    {
        float len = (float)Math.sqrt(x * x + y * y);
        return shoot(p, (x / len) * speed, (y / len) * speed);
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public int getMaxHealth()
    {
        return 1;
    }

    public int damage(int damage)
    {
        int newHealth = Math.max(0, getHealth() - damage);
        int damageDealt = getHealth() - newHealth;
        setHealth(newHealth);
        return damageDealt;
    }

    public void heal(int health)
    {
        setHealth(Math.min(getMaxHealth(), getHealth() + health));
    }
}
