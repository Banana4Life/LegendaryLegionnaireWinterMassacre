package life.banana4.ld31;

import java.util.concurrent.atomic.AtomicInteger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
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

    public void die()
    {
        getLevel().remove(this);
    }

    public Entity move(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public float getX()
    {
        return x;
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

    public abstract void draw(DrawContext ctx);

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

    public void onCollide(Entity a, Rectangle rect)
    {
    }

    public void reactTo(Intention intention, float delta)
    {

    }
}
