package life.banana4.ld31.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;

public class Player extends MovingEntity implements CollisionSource, CollisionTarget
{
    public static final float SPEED = 100;
    public static final float MINIMUM_MOVE_MUL = 0.06f;
    private boolean isMouseControlled = false;

    Map<Type, Float> waits = new HashMap<>();

    public Player()
    {
        super(20, 20);
        waits.put(Type.PRIMARY_ATTACK, 0f);
        waits.put(Type.SECONDARY_ATTACK, 0f);
        waits.put(Type.TERTIARY_ATTACK, 0f);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        for (Type type : waits.keySet())
        {
            waits.put(type, waits.get(type) + delta);
        }
    }

    @Override
    public void draw(DrawContext ctx)
    {
        super.draw(ctx);
        SpriteBatch batch = ctx.getSpriteBatch();
        batch.begin();

        Vector2 offset = new Vector2(-64, -64).rotate(getRotation() + 90);
        batch.draw(ctx.resources.textures.torso, getX() + this.getWidth() / 2 + offset.x,
                   getY() + this.getHeight() / 2 + offset.y, 0, 0, 128, 128, 1, 1, getRotation() + 90, 0, 0, 128, 128,
                   false, false);
        batch.end();

        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Line);
        r.setColor(Color.CYAN);
        Vector2 line = new Vector2(100, 0).setAngle(getRotation()).scl(100);
        r.line(getMidX(), getMidY(), getMidX() + line.x, getMidY() + line.y);
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
                    if (waits.get(t) <= 1f)
                    {
                        break;
                    }
                    shoot(new Projectile(this, 3, 3), dir.x, dir.y, 600);
                    waits.put(t, 0f);
                    break;
                case SECONDARY_ATTACK:
                    if (waits.get(t) <= 2f)
                    {
                        break;
                    }
                    Random random = this.getLevel().getRandom();
                    dir.setAngle(getRotation() - (random.nextInt(8) + 13));
                    shoot(new Projectile(this, 2, 2), dir.x, dir.y, 200);
                    dir.setAngle(getRotation() - (random.nextInt(8) + 5));
                    shoot(new Projectile(this, 2, 2), dir.x, dir.y, 200);
                    dir.setAngle(getRotation() + (random.nextInt(11) - 5));
                    shoot(new Projectile(this, 2, 2), dir.x, dir.y, 200);
                    dir.setAngle(getRotation() + (random.nextInt(8) + 5));
                    shoot(new Projectile(this, 2, 2), dir.x, dir.y, 200);
                    dir.setAngle(getRotation() + (random.nextInt(8) + 13));
                    shoot(new Projectile(this, 2, 2), dir.x, dir.y, 200);
                    waits.put(t, 0f);
                    //radius -= 15 * delta;
                    break;
                case TERTIARY_ATTACK:
                    if (waits.get(t) <= 10f)
                    {
                        break;
                    }
                    for (float i = 0; i < 360 ; i += 5)
                    {
                        dir.setAngle(getRotation() + i);
                        shoot(new Projectile(this, 5, 5), dir.x, dir.y, 500);
                    }
                    waits.put(t, 0f);
                    break;
            }
        }
    }

    @Override
    public void onCollide(CollisionTarget target, Vector2 mtv)
    {
        if (target instanceof Wall)
        {
            move(mtv.x, mtv.y);
        }
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {

    }

    @Override
    public Entity move(float x, float y)
    {
        super.move(x, y);
        if (this.isMouseControlled)
        {
            this.lookAt(Gdx.input.getX(), Gdx.input.getY());
        }
        return this;
    }

    public void setMouseControlled(boolean mouseControlled)
    {
        this.isMouseControlled = mouseControlled;
    }

    public void lookAt(int screenX, int screenY)
    {
        OrthographicCamera camera = this.getLevel().getGame().getDrawContext().camera;
        Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
        this.setRotation(new Vector2(pos.x - this.getX(), pos.y - this.getY()).angle());
    }
}
