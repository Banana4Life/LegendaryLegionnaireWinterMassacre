package life.banana4.ld31.entity;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
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
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.BoltProjectile;
import life.banana4.ld31.entity.projectile.DummyProjectile;
import life.banana4.ld31.input.AllThemInputAdapter;
import life.banana4.ld31.input.AllThemInputProcessor;
import life.banana4.ld31.input.ControllerIntentionDetector;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;
import life.banana4.ld31.input.XBox360Pad;
import life.banana4.ld31.resource.Animations;

import static java.lang.Math.abs;
import static life.banana4.ld31.Ld31.isDebug;

public class Player extends LivingEntity implements CollisionSource, CollisionTarget
{
    private static final float PRIMARY_COOLDOWN = 0.9f;
    private static final float SECONDARY_COOLDOWN = 0.45f;
    public static final float SPEED = 180f;
    private boolean isMouseControlled = false;
    private float walkingAngle = 0;

    private float sprint = 1;
    private boolean exhausted = false;

    private float primaryStateTime = 0;
    private float secondaryStateTime = SECONDARY_COOLDOWN;
    private float legStateTime = 0;
    private final AllThemInputProcessor inputProcessor = new PlayerInputHandler();

    Map<Type, Float> waits = new HashMap<>();
    private float sprintTime = 0f;

    public Player()
    {
        super(20, 20);
        setDepth(100);
        waits.put(Type.PRIMARY_ATTACK, 0f);
        waits.put(Type.SECONDARY_ATTACK, 0f);
        waits.put(Type.TERTIARY_ATTACK, 0f);
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        getLevel().getGame().getInputMultiplexer().append(inputProcessor);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();
        getLevel().getGame().getInputMultiplexer().remove(inputProcessor);
    }

    public float getWalkingAngle()
    {
        return walkingAngle;
    }

    @Override
    public int getMaxHealth()
    {
        return 100;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        if (sprintTime >= 1)
        {
            exhausted = true;
            sprintTime = 1;
        }
        if (sprint > 1)
        {
            sprintTime += delta;
        }
        else
        {
            sprintTime -= delta;
            if (sprintTime < 0)
            {
                sprintTime = 0;
                exhausted = false;
            }
        }

        super.update(camera, delta);
        legStateTime += delta;
        secondaryStateTime += delta;
        if (secondaryStateTime > SECONDARY_COOLDOWN)
        {
            primaryStateTime += delta;
        }

        for (Type type : waits.keySet())
        {
            waits.put(type, waits.get(type) + delta);
        }

        if (this.isMouseControlled)
        {
            Cursor c = getLevel().getCursor();
            this.setRotation(new Vector2(c.getX() - getX(), c.getY() - getY()).angle());
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

        if (vx == 0 && vy == 0) {
            legStateTime = 0;
        }

        if (!exhausted && sprint == 2f) {
            animations.legs.setFrameDuration(0.025f);
        } else {
            animations.legs.setFrameDuration(0.05f);
        }

        float modifiedWalkingAngle;
        float difference = Math.abs(getRotation() - getWalkingAngle());
        if (difference < 90 || difference > 270)
        {
            animations.legs.setPlayMode(PlayMode.NORMAL);
            modifiedWalkingAngle = getWalkingAngle();
        }
        else
        {
            animations.legs.setPlayMode(PlayMode.REVERSED);
            modifiedWalkingAngle = getWalkingAngle() + 180;
        }
        Vector2 walkingOffset = new Vector2(64, -64).rotate(modifiedWalkingAngle + 90);
        batch.draw(animations.legs.getKeyFrame(legStateTime, true), getMidX() + walkingOffset.x,
                   getMidY() + walkingOffset.y, 0, 0, 128, 128, 1, 1, modifiedWalkingAngle + 180, true);
        if (secondaryStateTime <= SECONDARY_COOLDOWN)
        {
            batch.draw(animations.charswordswing.getKeyFrame(secondaryStateTime), getMidX() + offset.x,
                       getMidY() + offset.y, 0, 0, 128, 128, 1, 1, getRotation() + 180, true);
        }
        else
        {
            batch.draw(animations.charcrossload.getKeyFrame(primaryStateTime), getMidX() + offset.x,
                       getMidY() + offset.y, 0, 0, 128, 128, 1, 1, getRotation() + 180, true);
        }
        batch.end();

        if (isDebug())
        {
            ShapeRenderer r = ctx.getShapeRenderer();
            r.begin(ShapeType.Line);
            r.setColor(Color.CYAN);
            Vector2 line = new Vector2(100, 0).setAngle(getRotation()).scl(100);
            r.line(getMidX(), getMidY(), getMidX() + line.x, getMidY() + line.y);

            r.setColor(Color.RED);
            r.line(getMidX(), getMidY(), getLevel().getCursor().getMidX(), getLevel().getCursor().getMidY());

            r.end();

            float rotation = this.getRotation();
            float min = rotation - 75;
            float max = rotation + 75;

            r.begin(ShapeType.Line);
            r.setColor(Color.BLUE);
            r.arc(this.getMidX(), this.getMidY(), 64, min, max - min);

            r.end();

            r.begin(ShapeType.Filled);
            r.setColor(Color.RED);
            Vector2 rotate = new Vector2(32, 3).rotate(this.getRotation());
            r.box(this.getMidX() + rotate.x, this.getMidY() + rotate.y, 0, 3, 3, 0);
            r.end();
        }
    }

    private static final Vector2 HELPER = new Vector2(0, 0);

    @Override
    public void reactTo(Intention intention, float delta)
    {
        Type t = intention.getType();
        if (t == Type.SPRINT)
        {
            sprint = 2f;
        }
        if (t == Type.NO_SPRINT)
        {
            sprint = 1f;
        }
        if (t == Type.MOVE)
        {
            Vector2 dir = intention.getArgumentAs(Vector2.class);
            float scaleX = abs(dir.x);
            float scaleY = abs(dir.y);
            dir.nor().scl(SPEED * (exhausted ? 1 : sprint)).scl(scaleX, scaleY);
            setVelocity(dir.x, dir.y);
            //move(dir.x, dir.y);
            this.walkingAngle = HELPER.set(dir.scl(dir.len())).angle();
        }
        else if (t == Type.HALT)
        {
            setVelocity(0, 0);
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
                    primaryStateTime = 0;
                    Vector2 rotate = new Vector2(32, 3).rotate(this.getRotation());
                    BoltProjectile bolt = new BoltProjectile(this);
                    bolt.move(this.getMidX() + rotate.x, this.getMidY() + rotate.y);
                    shoot(bolt, dir.x, dir.y);
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
                                    ((Enemy)entity).damage(25);
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
                    for (float i = 0; i < 360; i += 3)
                    {
                        dir.setAngle(getRotation() + i);
                        shoot(new DummyProjectile(this, 5, 5), dir.x, dir.y).move(this.getMidX(), this.getMidY());
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
        else if (target instanceof Enemy)
        {
            this.damage(1);
        }
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Projectile && ((Projectile)source).getShooter() instanceof Enemy)
        {
            ((Projectile)source).dealDamage(this);
        }
    }

    public void setMouseControlled(boolean mouseControlled)
    {
        this.isMouseControlled = mouseControlled;
    }

    @Override
    public boolean mayCollideWith(CollisionTarget target)
    {
        return !(target instanceof Projectile && ((Projectile)target).getShooter() == this);
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return true;
    }

    private final class PlayerInputHandler extends AllThemInputAdapter
    {

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            Player.this.setMouseControlled(true);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY)
        {
            Player.this.setMouseControlled(true);
            return true;
        }

        @Override
        public boolean axisMoved(Controller c, int axisCode, float value)
        {
            if (Math.abs(value) < ControllerIntentionDetector.MINIMUM_MOVE)
            {
                return false;
            }
            if (axisCode == XBox360Pad.AXIS_RIGHT_X || axisCode == XBox360Pad.AXIS_RIGHT_Y)
            {
                Vector2 vec = new Vector2(c.getAxis(XBox360Pad.AXIS_RIGHT_X), c.getAxis(XBox360Pad.AXIS_RIGHT_Y));
                Player.this.setRotation(vec.angle());
            }
            Player.this.setMouseControlled(false);
            return true;
        }
    }
}
