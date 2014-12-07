package life.banana4.ld31.entity;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.Ld31Resources;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.BoltProjectile;
import life.banana4.ld31.entity.projectile.DummyProjectile;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;
import life.banana4.ld31.resource.Animations;

import static java.lang.Math.abs;
import static life.banana4.ld31.Ld31.isDebug;

public class Player extends LivingEntity implements CollisionSource, CollisionTarget
{
    private static final float PRIMARY_COOLDOWN = 0.9f;
    private static final float SECONDARY_COOLDOWN = 0.45f;
    public static final float SPEED = 3.3f;
    public static final float MINIMUM_MOVE_MUL = 0.06f;
    private boolean isMouseControlled = false;
    private float walkingAngle = 0;

    private float primaryStateTime = 0;
    private float secondaryStateTime = SECONDARY_COOLDOWN;

    Map<Type, Float> waits = new HashMap<>();

    public Player()
    {
        super(20, 20);
        waits.put(Type.PRIMARY_ATTACK, 0f);
        waits.put(Type.SECONDARY_ATTACK, 0f);
        waits.put(Type.TERTIARY_ATTACK, 0f);
    }

    public float getWalkingAngle()
    {
        return walkingAngle;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        secondaryStateTime += delta;
        if (secondaryStateTime > SECONDARY_COOLDOWN)
        {
            primaryStateTime += delta;
        }

        for (Type type : waits.keySet())
        {
            waits.put(type, waits.get(type) + delta);
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();
        Animations animations = ctx.resources.animations;
        batch.begin();

        Vector2 offset = new Vector2(64, -64).rotate(getRotation() + 90);

        float modifiedWalkingAngle;
        float difference = Math.abs(getRotation() - getWalkingAngle());
        if (difference < 90 || difference > 270) {
            animations.legs.setPlayMode(PlayMode.NORMAL);
            modifiedWalkingAngle = getWalkingAngle();
        } else
        {
            animations.legs.setPlayMode(PlayMode.REVERSED);
            modifiedWalkingAngle = getWalkingAngle() + 180;
        }
        Vector2 walkingOffset = new Vector2(64, -64).rotate(modifiedWalkingAngle + 90);
        batch.draw(ctx.resources.animations.legs.getKeyFrame(primaryStateTime, true),
                   getX() + this.getWidth() / 2 + walkingOffset.x, getY() + this.getHeight() / 2 + walkingOffset.y, 0, 0, 128, 128, 1,
                   1, modifiedWalkingAngle + 180, true);
        if (secondaryStateTime <= SECONDARY_COOLDOWN) {
            batch.draw(ctx.resources.animations.charswordswing.getKeyFrame(secondaryStateTime),
                       getX() + this.getWidth() / 2 + offset.x, getY() + this.getHeight() / 2 + offset.y, 0, 0, 128,
                       128, 1, 1, getRotation() + 180, true);
        }
        else
        {
            batch.draw(ctx.resources.animations.charcrossload.getKeyFrame(primaryStateTime),
                       getX() + this.getWidth() / 2 + offset.x, getY() + this.getHeight() / 2 + offset.y, 0, 0, 128,
                       128, 1, 1, getRotation() + 180, true);
        }
        batch.end();

        if (isDebug())
        {
            ShapeRenderer r = ctx.getShapeRenderer();
            r.begin(ShapeType.Line);
            r.setColor(Color.CYAN);
            Vector2 line = new Vector2(100, 0).setAngle(getViewingAngle()).scl(100);
            r.line(getMidX(), getMidY(), getMidX() + line.x, getMidY() + line.y);
            r.end();

            float rotation = this.getRotation();
            float min = rotation - 75;
            float max = rotation + 75;

            r.begin(ShapeType.Line);
            r.setColor(Color.BLUE);
            r.arc(this.getMidX(), this.getMidY(), 64, min, max - min);

            r.end();

        }
    }

    private static final Vector2 HELPER = new Vector2(0, 0);

    @Override
    public void reactTo(Intention intention, float delta)
    {
        Type t = intention.getType();
        if (t == Type.MOVE)
        {
            Vector2 dir = intention.getArgumentAs(Vector2.class);
            float scaleX = abs(dir.x);
            float scaleY = abs(dir.y);
            if (dir.len2() < MINIMUM_MOVE_MUL * MINIMUM_MOVE_MUL)
            {
                return;
            }
            dir.nor().scl(SPEED).scl(scaleX, scaleY);
            move(dir.x, dir.y);
            this.walkingAngle = HELPER.set(dir.scl(dir.len())).angle();
        }
        else
        {
            Vector2 dir = new Vector2(1, 0).setAngle(getRotation());
            switch (t)
            {
                case PRIMARY_ATTACK:
                    if (waits.get(t) <= PRIMARY_COOLDOWN || waits.get(Type.SECONDARY_ATTACK) <= SECONDARY_COOLDOWN)
                    {
                        break;
                    }
                    Ld31Resources resources = this.getLevel().getGame().getDrawContext().resources;
                    primaryStateTime = 0;
                    shoot(new BoltProjectile(this),
                          dir.x, dir.y, 600);
                    waits.put(t, 0f);
                    break;
                case SECONDARY_ATTACK:
                    if (waits.get(t) <= SECONDARY_COOLDOWN)
                    {
                        break;
                    }
                    secondaryStateTime = 0;
                    waits.put(t, 0f);
                    Vector2 tmp = new Vector2();

                    float rotation = this.getRotation();
                    float min = rotation - 75;
                    float max = rotation + 75;
                    if (max < min)
                    {
                        max += min;
                        min = max - min;
                        max -= min;
                    }
                    for (Entity entity : this.getLevel().getEntities())
                    {
                        if (entity instanceof Enemy)
                        {
                            float vX = entity.getMidX() - this.getMidX();
                            float vY = entity.getMidY() - this.getMidY();
                            if (vX * vX + vY * vY <= 64 * 64)
                            {
                                tmp.set(vX, vY);
                                float angle = tmp.angle();
                                if (min < angle && max > angle)
                                {
                                    ((Enemy)entity).damage(1);
                                }
                            }
                        }
                    }

                    //radius -= 15 * delta;
                    break;
                case TERTIARY_ATTACK:
                    if (waits.get(t) <= 10f)
                    {
                        break;
                    }
                    for (float i = 0; i < 360; i += 5)
                    {
                        dir.setAngle(getRotation() + i);
                        shoot(new DummyProjectile(this, 5, 5), dir.x, dir.y, 500);
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

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return !(target instanceof Projectile && ((Projectile)target).getShooter() == this);
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return false;
    }
}
