package life.banana4.ld31;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity
{
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    public final int id;
    private Level level;
    private float x;
    private float y;
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

    public void move(float x, float y)
    {
        this.x += x;
        this.y += y;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public abstract void update(float delta);

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
}
