package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.Particle;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.Snowman;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class ShipRocket extends Projectile implements CollisionTarget
{
    private Entity target;

    public ShipRocket(AlienShip ship, Entity target)
    {
        super(ship, 30, 10, 10);
        this.target = target;
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();
        Texture tex = this.getLevel().getGame().getDrawContext().resources.textures.shipprojectile;
        Vector2 rotate = new Vector2(-9, -8).rotate(getViewingAngle() + 90);
        batch.begin();
        batch.draw(tex, getX() + getWidth() / 2 + rotate.x, getY() + getHeight() / 2 + rotate.y, 0, 0, 16, 48, 1, 1,
                   getViewingAngle() + 90, 0, 0, 16, 48, false, false);
        batch.end();
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        getLevel().getGame().getDrawContext().resources.sounds.rocketdeath.play();
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        float speed = (float)Math.sqrt(vx * vx + vy * vy);
        this.setSpeed(target.getMidX() - getMidX(), target.getMidY() - getMidY(), speed);
    }

    @Override
    public float getSpeed()
    {
        return 1000;
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return target == this.target;
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Snowman)
        {
            kill();
        }
        if (target instanceof Player)
        {
            dealDamage((Player)target);
            getLevel().addEntity(new Particle(
                getLevel().getGame().getDrawContext().resources.particles.rocketexplosion).move(getMidX(), getMidY()));
        }
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Bolt || source instanceof FireProjectile)
        {
            this.kill();
        }
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Bolt || source instanceof FireProjectile;
    }
}
