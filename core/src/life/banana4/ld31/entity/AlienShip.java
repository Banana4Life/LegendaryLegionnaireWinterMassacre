package life.banana4.ld31.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.enemy.Unicorn;
import life.banana4.ld31.entity.enemy.EnemyWalker;
import life.banana4.ld31.entity.projectile.Bolt;
import life.banana4.ld31.entity.projectile.FireProjectile;
import life.banana4.ld31.entity.projectile.Projectile;
import life.banana4.ld31.entity.projectile.ShipRocket;

public class AlienShip extends LivingEntity implements CollisionTarget
{
    private static final float SCALE = 2;
    private float ANGULAR_VELOCITY = 16;
    private float SPAWN_DELAY = 1f;
    private float ROCKET_DELAY = 5f;
    private final Snowman snowman;
    private static final Vector2 TARGET = new Vector2(540, 100);

    private boolean rocketShot = false;
    private boolean passed = false;
    private boolean endFight = false;
    private boolean endFight2 = false;

    private float waitedSpawn = SPAWN_DELAY;
    private float waitedRocket = ROCKET_DELAY;
    private boolean damaged = false;

    public AlienShip(Snowman snowman)
    {
        super(size(128 * SCALE), size(128 * SCALE));
        this.snowman = snowman;
        setDepth(200);
        setPosition(Gdx.graphics.getWidth() / 2, -getHeight());
        setSpeed(3, 2, 180);
    }

    private static float size(float d)
    {
        final float r = d / 2f;
        return 2 * (float)Math.sqrt((r * r) / 2f);
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

        if (getMidX() >= Gdx.graphics.getWidth() / 4 * 3 && !rocketShot)
        {
            this.rocketShot = true;
            shoot(new ShipRocket(this, snowman), snowman.getMidX() - getMidX(), snowman.getMidY() - getMidY()).move(getMidX(),
                                                                                                          getMidY());
        }

        setRotation(getRotation() + delta * ANGULAR_VELOCITY);

        if (getX() <= TARGET.x && endFight)
        {
            setVelocity(0, 0);
            if (endFight)
            {
                Player p = getLevel().getPlayer();
                if (!p.isDead())
                {
                    if (getHealth()*1f / getMaxHealth() < 0.2 && !endFight2)
                    {
                        System.out.println(getHealth());
                        endFight2 = true;
                        ROCKET_DELAY = 3f;
                        ANGULAR_VELOCITY = 160;
                    }
                    waitedRocket += delta;
                    waitedSpawn += delta;
                    if (waitedSpawn >= SPAWN_DELAY)
                    {
                        waitedSpawn = 0;

                        if (endFight2)
                        {
                            getLevel().addEntity(new Unicorn().move(getMidX(), getMidY()));
                        }
                        else
                        {
                            getLevel().addEntity(new EnemyWalker(0).move(getMidX(), getMidY()));
                        }
                    }
                    if (waitedRocket >= ROCKET_DELAY)
                    {
                        waitedRocket = 0;
                        ShipRocket rocket = shoot(new ShipRocket(this, p), 0, 0);
                        rocket.move(getMidX(), getMidY());
                        rocket.setSpeed(p.getMidX() - getMidX(), p.getMidY() - getMidY(), 300);
                    }
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
        if (damaged)
        {
            damaged = false;
            batch.setColor(Color.RED);
        }
        batch.draw(t, getMidX() - pos.x, getMidY() - pos.y, 0, 0, 128, 128, SCALE, SCALE, getRotation(), 0, 0, 128, 128,
                   false, true);
        batch.setColor(Color.WHITE);
        batch.end();
    }

    @Override
    public void onOutsideWorld()
    {
        if (getX() > Gdx.graphics.getWidth() + 20 * SCALE && !endFight)
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

    @Override
    public int damage(int damage)
    {
        this.damaged = true;
        return super.damage(damage);
    }
}
