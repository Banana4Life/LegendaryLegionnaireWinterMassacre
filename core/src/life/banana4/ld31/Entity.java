package life.banana4.ld31;

public abstract class Entity
{
    private Level level;
    private float x;
    private float y;
    private final float width;
    private final float height;

    public Entity(float width, float height)
    {
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

    public void die() {
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

    public abstract void draw(float delta);
}
