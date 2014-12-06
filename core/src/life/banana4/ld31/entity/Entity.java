package life.banana4.ld31.entity;

public abstract class Entity
{
    private float x;
    private float y;
    private final float width;
    private final float height;

    public Entity(float width, float height)
    {
        this.width = width;
        this.height = height;
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
}
