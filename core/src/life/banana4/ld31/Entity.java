package life.banana4.ld31;

import java.util.concurrent.atomic.AtomicInteger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.input.Intention;

public abstract class Entity
{
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    public final int id;
    private Level level;
    private float x;
    private float y;
    private float rotation = 0;
    private final float width;
    private final float height;
    private boolean dead = false;

    public Entity(float width, float height)
    {
        this.id = COUNTER.getAndIncrement();
        this.width = width;
        this.height = height;
    }

    public Level getLevel()
    {
        return level;
    }

    void setLevel(Level level)
    {
        this.level = level;
    }

    public void onSpawn()
    {
    }

    public void kill()
    {
        getLevel().remove(this);
        this.onDeath();
        this.dead = true;
    }

    public boolean isDead()
    {
        return dead;
    }

    public void onDeath()
    {
    }

    public Entity move(float x, float y)
    {
        this.x += x;
        this.y += y;

        if (this.x < 0 || this.y < 0 || this.x > Gdx.graphics.getWidth() || this.y > Gdx.graphics.getHeight())
        {
            this.kill();
        }
        return this;
    }

    public float getX()
    {
        return x;
    }

    public float getMidX()
    {
        return getX() + getWidth() / 2;
    }

    public float getMidY()
    {
        return getY() + getHeight() / 2;
    }

    public float getY()
    {
        return y;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public float getRotation()
    {
        return rotation;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public abstract void update(OrthographicCamera camera, float delta);

    public void draw(DrawContext ctx, float delta)
    {
        if (this.getLevel().isDebug())
        {
            ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.box(this.getX(), this.getY(), 0, this.getWidth(), this.getHeight(), 0);
            shapeRenderer.end();
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || !(o instanceof Entity))
        {
            return false;
        }

        return ((Entity)o).id == id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    public void reactTo(Intention intention, float delta)
    {

    }
}
