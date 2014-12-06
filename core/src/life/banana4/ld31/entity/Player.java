package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;

public class Player extends MovingEntity implements CollisionSource, CollisionTarget
{
    public static final float SPEED = 100;
    public static final float MINIMUM_MOVE_MUL = 0.06f;

    public Player()
    {
        super(10, 10);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
    }

    @Override
    public void draw(DrawContext ctx)
    {
        SpriteBatch batch = ctx.getSpriteBatch();
        batch.begin();

        Vector2 offset = new Vector2(-64, -64).rotate(getRotation() + 90);
        batch.draw(ctx.resources.textures.torso, getX() + offset.x, getY() + offset.y, 0, 0, 128, 128, 1, 1,
                   getRotation() + 90, 0, 0, 128, 128, false, false);
        batch.end();

        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Line);
        r.setColor(Color.CYAN);
        Vector2 line = new Vector2(100, 0).setAngle(getRotation()).scl(100);
        r.line(getX(), getY(), getX() + line.x, getY() + line.y);
        r.end();
    }

    @Override
    public void reactTo(Intention intention, float delta)
    {
        Type t = intention.getType();
        if (t.isMove())
        {
            Float mul = intention.getArgumentOr(1f);
            if (mul < MINIMUM_MOVE_MUL)
            {
                return;
            }
            mul *= delta;
            switch (t)
            {
                case MOVE_UP:
                    move(0 * mul, -SPEED * mul);
                    break;
                case MOVE_DOWN:
                    move(0 * mul, SPEED * mul);
                    break;
                case MOVE_LEFT:
                    move(-SPEED * mul, 0 * mul);
                    break;
                case MOVE_RIGHT:
                    move(SPEED * mul, 0 * mul);
                    break;
            }
        }
        else
        {
            Vector2 dir = new Vector2(1, 0).setAngle(getRotation());
            switch (t)
            {
                case PRIMARY_ATTACK:
                    shoot(new Projectile(this, 3, 3), dir.x, dir.y, 500);
                    break;
                case SECONDARY_ATTACK:
                    //radius -= 15 * delta;
                    break;
            }
        }
    }

    @Override
    public void onCollide(Rectangle rect, CollisionTarget target)
    {

    }

    @Override
    public void onCollide(Rectangle rect, CollisionSource source)
    {

    }
}
