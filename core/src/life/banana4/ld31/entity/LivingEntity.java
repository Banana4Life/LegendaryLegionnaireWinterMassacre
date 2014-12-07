package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.Entity;

public abstract class LivingEntity extends Entity
{
    protected float vx;
    protected float vy;
    private long aliveSince = -1;
    private float viewingAngle = 0;

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

    public void setSpeed(float vx, float vy, float speed)
    {
        if (speed == 1)
        {
            setVelocity(vx, vy);
            return;
        }
        float len = (float)Math.sqrt(vx * vx + vy * vy);
        setVelocity((vx / len) * speed, (vy / len) * speed);
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        aliveSince = System.currentTimeMillis();
    }

    private static final Vector2 V = new Vector2(0, 0);

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        this.viewingAngle = V.set(vx, vy).angle();
        this.move(vx * delta, vy * delta);
        if (getHealth() <= 0)
        {
            this.kill();
        }
    }

    public Projectile shoot(Projectile p, float x, float y)
    {
        getLevel().addEntity(p);
        p.setSpeed(x, y, p.getSpeed());
        p.setRotation(new Vector2(x, y).angle());
        return p;
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

    public long lifetime()
    {
        return System.currentTimeMillis() - this.aliveSince;
    }

    public float getViewingAngle()
    {
        return viewingAngle;
    }
}
