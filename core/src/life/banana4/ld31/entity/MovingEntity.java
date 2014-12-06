package life.banana4.ld31.entity;

public abstract class MovingEntity extends Entity
{
    private float vx;
    private float vy;

    public MovingEntity(float width, float height)
    {
        super(width, height);
    }

    public void accelerate(float x, float y)
    {
        this.vx += x;
        this.vy += y;
    }
}
