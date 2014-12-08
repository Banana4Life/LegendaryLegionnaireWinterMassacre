package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.Projectile;
import life.banana4.ld31.entity.Snowman;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class ShipLaser extends Projectile implements CollisionSource
{
    public ShipLaser(AlienShip ship)
    {
        super(ship, 0, 10, 10);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();
        Texture tex = this.getLevel().getGame().getDrawContext().resources.textures.shipprojectile;
        Vector2 rotate = new Vector2(-24, -8).rotate(getRotation() + 90);
        batch.begin();
        batch.draw(tex, getX() + getWidth() / 2 + rotate.x, getY() + getHeight() / 2 + rotate.y, 0, 0, 16, 48, 1, 1,
               getRotation() + 90, 0, 0, 16, 48, false, false);
        batch.end();
    }

    @Override
    public float getSpeed()
    {
        return 1000;
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return target instanceof Snowman;
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Snowman)
        {
            kill();
        }
    }
}
