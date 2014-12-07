package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class Projectile extends MovingEntity implements CollisionSource
{
    private final Entity shooter;
    private final Texture texture;

    public Projectile(Entity shooter, float width, float height)
    {
        this(width, height, shooter, null);
    }

    public Projectile(float width, float height, Entity shooter, Texture texture)
    {
        super(width, height);
        this.shooter = shooter;
        this.texture = texture;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
    }

    @Override
    public void draw(DrawContext ctx)
    {
        super.draw(ctx);

        if (texture == null)
        {
            ShapeRenderer r = ctx.getShapeRenderer();

            r.begin(ShapeType.Filled);
            if (shooter instanceof Player)
            {
                r.setColor(Color.BLUE);
                r.circle(getMidX(), getMidY(), getWidth());
            }
            else
            {
                r.setColor(Color.RED);
                r.circle(getMidX(), getMidY(), 3);
            }

            r.end();
        }
        else
        {
            SpriteBatch spriteBatch = ctx.getSpriteBatch();
            spriteBatch.begin();
            Vector2 rotate = new Vector2(-16, 0).rotate(getRotation() + 90);
            spriteBatch.draw(texture, this.getX() + rotate.x, this.getY() + rotate.y, 0, 0, 32, 32, 1, 1, this.getRotation() + 90, 0, 0, 32, 32,
                             false, false);
            spriteBatch.end();
        }
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Wall)
        {
            die();
        }
        if (shooter instanceof Player && target instanceof Enemy)
        {
            ((Enemy)target).die();
            die();
        }
    }

    public static Projectile of(Texture texture, Entity shooter)
    {
        return new Projectile(4, 4, shooter, texture);
    }
}
