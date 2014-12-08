package life.banana4.ld31.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.Ld31;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;
import life.banana4.ld31.entity.projectile.Bolt;
import life.banana4.ld31.entity.projectile.FireProjectile;
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
    public static final float PRIMARY_COOLDOWN = 0.9f;
    public static final float SECONDARY_COOLDOWN = 0.45f;
    public static final float TERTIARY_COOLDOWN = 2f;
    public static final float SPEED = 180f;
    public static final float SECONDARY_RADIUS = 72f;
    private boolean isMouseControlled = false;
    private float walkingAngle = 0;

    private float sprint = 1;
    private boolean exhausted = false;

    private float primaryStateTime = 0;
    private float secondaryStateTime = SECONDARY_COOLDOWN;
    private float legStateTime = 0;
    private int lastLegState = 0;
    private boolean lastFootLeft = false;
    private final AllThemInputProcessor inputProcessor = new PlayerInputHandler();
    private int bombs = 0;

    Map<Type, Float> waits = new HashMap<>();
    private float sprintTime = 0f;

    public Player()
    {
        super(20, 20);
        setDepth(100);
        waits.put(Type.PRIMARY_ATTACK, PRIMARY_COOLDOWN);
        waits.put(Type.SECONDARY_ATTACK, SECONDARY_COOLDOWN);
        waits.put(Type.TERTIARY_ATTACK, TERTIARY_COOLDOWN);
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

        for (final Entity entity : getLevel().getEntities())
        {
            if (entity instanceof Enemy || entity instanceof Projectile)
            {
                entity.kill();
            }
        }
    }

    public float getWalkingAngle()
    {
        return walkingAngle;
    }

    @Override
    public int getMaxHealth()
    {
        return Ld31.isDebug() ? 999999999 : 100;
    }

    public float getStamina() {
        return sprintTime;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        exhausted = false;
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
            }
        }

        super.update(camera, delta);
        legStateTime += delta;
        secondaryStateTime += delta;
        if (secondaryStateTime > SECONDARY_COOLDOWN)
        {
            primaryStateTime += delta;
        }
        else
        {
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
                    if (vX * vX + vY * vY <= SECONDARY_RADIUS * SECONDARY_RADIUS)
                    {
                        tmp.set(vX, vY);
                        float angle = tmp.angle();
                        if (min < angle && max > angle)
                        {
                            ((Enemy)entity).damage(5);
                        }
                    }
                    else if (vX * vX + vY * vY <= 24 * 24)
                    {
                        ((Enemy)entity).damage(5);
                    }
                }
            }
        }

        for (Type type : waits.keySet())
        {
            waits.put(type, waits.get(type) + delta);
        }

        if (this.isMouseControlled)
        {
            Cursor c = getLevel().getCursor();
            this.setRotation(new Vector2(c.getX() - getMidX(), c.getY() - getMidY()).angle());
        }

        if (vx == 0 && vy == 0)
        {
            legStateTime = 0;
        }

        Animations animations = getLevel().getGame().getDrawContext().resources.animations;

        int legState = (int)(legStateTime / animations.legs.getFrameDuration());
        legState %= animations.legs.getKeyFrames().length;

        float footAngle = getWalkingAngle();
        if (lastLegState != legState)
        {
            lastLegState = legState;
            if (legState % 7 == 0)
            {
                if (legStateTime == 0)
                {
                    getLevel().addEntity(new FootStep(true)).move(getMidX(), getMidY()).setRotation(footAngle);
                    getLevel().addEntity(new FootStep(false)).move(getMidX(), getMidY()).setRotation(footAngle);
                    lastFootLeft = false;
                }
                else
                {
                    getLevel().addEntity(new FootStep(lastFootLeft)).move(getMidX(), getMidY()).setRotation(footAngle);
                    lastFootLeft = !lastFootLeft;
                }
            }
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

        if (!exhausted && sprint == 2f)
        {
            animations.legs.setFrameDuration(0.025f);
        }
        else
        {
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
            r.arc(this.getMidX(), this.getMidY(), SECONDARY_RADIUS, min, max - min);
            r.circle(this.getMidX(), this.getMidY(), 24);
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
                    Bolt bolt = new Bolt(this);
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

                    //radius -= 15 * delta;
                    break;
                case TERTIARY_ATTACK:
                    if (waits.get(t) <= TERTIARY_COOLDOWN || bombs <= 0)
                    {
                        break;
                    }
                    Random random = new Random(System.currentTimeMillis());
                    for (float i = 0; i < 360; i += 1.5f)
                    {
                        dir.setAngle(getRotation() + i);
                        shoot(new FireProjectile(this, 5, 5, getLevel().getGame().getDrawContext().resources.particles.fire), dir.x, dir.y).move(this.getMidX() + random.nextInt(20) - 10, this.getMidY() + random.nextInt(20) - 10);
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
        if (source instanceof Projectile && ((Projectile)source).getShooter() instanceof Enemy)
        {
            ((Projectile)source).dealDamage(this);
            getLevel().addEntity(new Particle(getLevel().getGame().getDrawContext().resources.particles.blood).move(
                getMidX(), getMidY()));
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

    public int getBombs()
    {
        return bombs;
    }

    public void addBombs(int bombs)
    {
        this.bombs += bombs;
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
