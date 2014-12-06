package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import life.banana4.ld31.Entity;

public class Collider
{
    private static final Rectangle helper1 = new Rectangle(0, 0, 0, 0);
    private static final Rectangle helper2 = new Rectangle(0, 0, 0, 0);
    private static final Rectangle helper3 = new Rectangle(0, 0, 0, 0);
    private static final Polygon polyHelper = new Polygon();
    private static final float[] vertexHelper = new float[8];

    public static Rectangle findCollision(Entity e1, Entity e2)
    {
        helper1.set(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
        helper2.set(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());
        if (Intersector.intersectRectangles(helper1, helper2, helper3))
        {
            return helper3;
        }
        return null;
    }
}
