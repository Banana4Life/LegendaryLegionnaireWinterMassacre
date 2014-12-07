package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.Projectile;
import life.banana4.ld31.entity.Wall;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class BoltProjectile extends Projectile
{
    public BoltProjectile(Entity shooter)
    {
        super(shooter, 4, 4);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch b = ctx.getSpriteBatch();

        Texture tex = this.getLevel().getGame().getDrawContext().resources.textures.bolt;
        Vector2 rotate = new Vector2(-16, -6).rotate(getRotation() + 90);
        b.begin();
        b.draw(tex, getX() + getWidth() / 2 + rotate.x, getY() + getHeight() / 2 + rotate.y, 0, 0, 32, 32, 1, 1,
               getRotation() + 90, 0, 0, 32, 32, false, false);
        b.end();
        this.damagePotential = 5;
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Wall)
        {
            setVelocity(0, 0);
            move(vx, vy);
        }
    }

    @Override
    public float getSpeed()
    {
        return 600;
    }
}
