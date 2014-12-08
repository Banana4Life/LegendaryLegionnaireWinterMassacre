package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.Bolt;
import life.banana4.ld31.entity.projectile.FireProjectile;
import life.banana4.ld31.entity.projectile.ShipLaser;

public class AlienShip extends LivingEntity implements CollisionTarget
{
    private static final float SCALE = 2;
    private static final float ANGULAR_VELOCITY = 16;
    private static final float SPAWN_DELAY = 1f;
    private static final float ROCKET_DELAY = 5f;
    private final Snowman snowman;
    private static final Vector2 TARGET = new Vector2(540, 100);

    private boolean laserShot = false;
    private boolean passed = false;
    private boolean endFight = false;
    private float waitedSpawn = SPAWN_DELAY;
    private float waitedRocket = ROCKET_DELAY;

    public AlienShip(Snowman snowman)
    {
        super(128 * SCALE, 128 * SCALE);
        this.snowman = snowman;
        setDepth(200);
        setPosition(-getWidth(), 25);
        setVelocity(180, 0);
    }

    @Override
    public int getMaxHealth()
    {
        return 100;
    }

    public boolean hasPassed()
    {
        return passed;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);

        if (getMidX() + 100 >= snowman.getMidX() && !laserShot)
        {
            this.laserShot = true;
            shoot(new ShipLaser(this, snowman), snowman.getMidX() - getMidX(), snowman.getMidY() - getMidY()).move(getMidX(),
                                                                                                          getMidY());
        }

        setRotation(getRotation() + delta * ANGULAR_VELOCITY);

        if (getX() <= TARGET.x && endFight)
        {
            setVelocity(0, 0);
            if (endFight)
            {
                waitedRocket += delta;
                waitedSpawn += delta;
                if (waitedSpawn >= SPAWN_DELAY)
                {
                    waitedSpawn = 0;
                    getLevel().addEntity(new PointEnemy(0).move(getMidX(), getMidY()));

                }
                if (waitedRocket >= ROCKET_DELAY)
                {
                    waitedRocket = 0;
                    Player p = getLevel().getPlayer();
                    ShipLaser rocket = shoot(new ShipLaser(this, p), 0, 0);
                    rocket.move(getMidX(), getMidY());
                    rocket.setSpeed(p.getMidX() - getMidX(), p.getMidY() - getMidY(), 300);
                }
            }
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();

        Texture t = ctx.resources.textures.ship;
        batch.begin();
        Vector2 pos = new Vector2(t.getWidth() * SCALE / 2, t.getHeight() * SCALE / 2);
        pos.rotate(getRotation());
        batch.draw(t, getMidX() - pos.x, getMidY() - pos.y, 0, 0, 128, 128, SCALE, SCALE, getRotation(), 0, 0, 128, 128,
                   false, true);
        batch.end();
    }

    @Override
    public void onOutsideWorld()
    {
        if (getX() > 0 && !endFight)
        {
            this.passed = true;
            setVelocity(0, 0);
        }
    }

    public void startEndFight()
    {
        if (!this.endFight)
        {
            this.endFight = true;
            this.setSpeed(TARGET.x - getX(), TARGET.y - getY(), 200);
        }
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Projectile)
        {
            ((Projectile)source).dealDamage(this);
        }
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Bolt || source instanceof FireProjectile;
    }
}
