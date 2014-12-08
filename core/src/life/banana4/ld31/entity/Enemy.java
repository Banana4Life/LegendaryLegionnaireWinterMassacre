package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.ai.TiledNode;
import life.banana4.ld31.ai.TiledSmoothableGraphPath;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.AlienLaser;

import static life.banana4.ld31.resource.Levels.TILE_WIDTH;
import static life.banana4.ld31.resource.Levels.TILE_WIDTH_2;

public abstract class Enemy extends LivingEntity implements CollisionSource, CollisionTarget
{
    private TiledSmoothableGraphPath path = new TiledSmoothableGraphPath();
    private float waitedFor = 0;
    private float melee = 0;

    private static final float ATTACK_RANGE = 300;
    private static final float SHOT_DELAY = 0.65f;

    private float damageable = Player.SECONDARY_COOLDOWN;


    public Enemy(float width, float height)
    {
        super(width, height);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        damageable -= delta;
        this.getLevel().calculatePath(path, this);
        if (path.getCount() == 0)
        {
            this.setVelocity(0, 0);
            return;
        }

        float x;
        float y;
        if (path.getCount() <= 2)
        {
            x = this.getLevel().getPlayer().getMidX() - this.getMidX();
            y = this.getLevel().getPlayer().getMidY() - this.getMidY();
        }
        else
        {
            TiledNode tiledNode = path.get(1);
            x = tiledNode.x * TILE_WIDTH + TILE_WIDTH_2 - this.getMidX();
            y = tiledNode.y * TILE_WIDTH + TILE_WIDTH_2 - this.getMidY();
        }
        double length = Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        this.setVelocity(x * getSpeed(), y * getSpeed());

        attack(delta);

        melee -= delta;
    }

    protected void attack(float delta)
    {
        Player p = getLevel().getPlayer();
        float dx = p.getX() - getX();
        float dy = p.getY() - getY();
        waitedFor += delta;
        if (dx * dx + dy * dy < ATTACK_RANGE * ATTACK_RANGE && waitedFor > SHOT_DELAY && (path.getCount() == 2 || path.getCount() == 3))
        {
            shoot(new AlienLaser(this), dx, dy).move(this.getMidX(), this.getMidY());
            waitedFor = 0;
        }
    }

    @Override
    public void onDeath()
    {
        getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.explosion).move(
            getMidX(), getMidY()));
        this.getLevel().addScore(getPoints());
    }

    public int getPoints()
    {
        return 25;
    }

    public float getSpeed()
    {
        return 130f;
    }


    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Projectile && ((Projectile)source).getShooter() instanceof Player)
        {
            ((Projectile)source).dealDamage(this);
        }
    }


    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Player && melee <= 0)
        {
            ((Player)target).damage(1);
            melee = 0.5f;
        }
    }


    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return !(target instanceof Projectile && ((Projectile)target).getShooter() instanceof Enemy);
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return true;
    }

    @Override
    public int damage(int damage)
    {
        if (damageable < 0)
        {
            damageable = Player.SECONDARY_COOLDOWN;
            return super.damage(damage);
        }
        return 0;
    }
}
