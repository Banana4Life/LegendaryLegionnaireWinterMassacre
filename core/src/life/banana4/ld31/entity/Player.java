package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.input.Intention;
import life.banana4.ld31.input.Intention.Type;

public class Player extends MovingEntity
{
    public static final float SPEED = 100;
    private float radius = 20;

    public Player()
    {
        super(10, 10);
    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void draw(DrawContext ctx)
    {
        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Filled);
        r.circle(getX(), getY(), radius);
        r.end();
    }

    @Override
    public void reactTo(Intention intention, float delta)
    {
        Type t = intention.getType();
        if (t.isMove())
        {
            Float mul = delta * intention.getArgumentOr(1f);
            switch (t)
            {
                case MOVE_UP:
                    move(0 * mul, SPEED * mul);
                    break;
                case MOVE_DOWN:
                    move(0 * mul, -SPEED * mul);
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
            switch (t)
            {
                case PRIMARY_ATTACK:
                    radius += 15 * delta;
                    break;
                case SECONDARY_ATTACK:
                    radius -= 15 * delta;
                    break;
            }
        }
    }
}
