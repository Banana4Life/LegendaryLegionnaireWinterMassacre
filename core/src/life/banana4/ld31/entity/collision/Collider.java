package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.Entity;

import static java.lang.Math.abs;

public class Collider
{
    private static final Vector2 amin = new Vector2(0, 0);
    private static final Vector2 amax = new Vector2(0, 0);
    private static final Vector2 bmin = new Vector2(0, 0);
    private static final Vector2 bmax = new Vector2(0, 0);

    private static final Vector2 mtv = new Vector2(0, 0);

    public static Vector2 findCollision(Entity a, Entity b)
    {
        amin.set(a.getX(), a.getY());
        amax.set(a.getX() + a.getWidth(), a.getY() + a.getHeight());
        bmin.set(b.getX(), b.getY());
        bmax.set(b.getX() + b.getWidth(), b.getY() + b.getHeight());

        mtv.set(0, 0);

        float left = bmin.x - amax.x;
        float right = bmax.x - amin.x;
        float top = bmin.y - amax.y;
        float bottom = bmax.y - amin.y;

        if (left > 0 || right < 0 || top > 0 || bottom < 0)
        {
            return null;
        }
        else
        {
            mtv.x = abs(left) < right ? left : right;
            mtv.y = abs(top) < bottom ? top : bottom;
            if (abs(mtv.x) < abs(mtv.y))
            {
                mtv.y = 0;
            }
            else
            {
                mtv.x = 0;
            }
            if (mtv.x == 0 && mtv.y == 0)
            {
                return null;
            }
            return mtv;
        }
    }
}
