package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public abstract class Projectile extends LivingEntity implements CollisionSource
{
    private int damagePotential;
    private final Entity shooter;

    protected Projectile(Entity shooter, int damagePotential, float width, float height)
    {
        super(width, height);
        this.shooter = shooter;
        this.damagePotential = damagePotential;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        if (damagePotential <= 0)
        {
            this.kill();
        }
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Wall)
        {
            kill();
        }
    }

    public Entity getShooter()
    {
        return shooter;
    }

    public void dealDamage(LivingEntity e)
    {
        this.damagePotential -= e.damage(this.damagePotential);
        if (this.damagePotential <= 0)
        {
            kill();
        }
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return !(target instanceof Projectile) && this.vx != 0 && this.vy != 0;
    }

    public abstract float getSpeed();
}
