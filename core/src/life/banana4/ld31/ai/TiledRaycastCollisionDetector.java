package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.util.TileType;

import static life.banana4.ld31.util.TileType.SNOW;

public class TiledRaycastCollisionDetector implements RaycastCollisionDetector<Vector2>
{
    TiledGraph worldMap;

    public TiledRaycastCollisionDetector(TiledGraph worldMap)
    {
        this.worldMap = worldMap;
    }

    @Override
    public boolean collides(Ray<Vector2> ray)
    {
        for (int i1 = 0; i1 <= 1; i1++)
        {
            for (int j1 = 0; j1 <= 1; j1++)
            {
                for (int i2 = 0; i2 <= 1; i2++)
                {
                    for (int j2 = 0; j2 <= 1; j2++)
                    {
                        if (collides0(ray.start.x + i1, ray.start.y + j1,
                                      ray.end.x + i2, ray.end.y + j2))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean collides0(float x0, float y0, float x1, float y1)
    {
        float tmp;
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep)
        {
            // Swap x0 and y0
            tmp = x0;
            x0 = y0;
            y0 = tmp;
            // Swap x1 and y1
            tmp = x1;
            x1 = y1;
            y1 = tmp;
        }
        if (x0 > x1)
        {
            // Swap x0 and x1
            tmp = x0;
            x0 = x1;
            x1 = tmp;
            // Swap y0 and y1
            tmp = y0;
            y0 = y1;
            y1 = tmp;
        }

        float deltax = x1 - x0;
        float deltay = Math.abs(y1 - y0);
        int error = 0;
        float y = y0;
        float ystep = (y0 < y1 ? 0.2f : -0.2f);
        for (float x = x0; x <= x1; x += 0.2f)
        {
            TiledNode tile = steep ? worldMap.getNode((int)y, (int)x) : worldMap.getNode((int)x, (int)y);
            if (!TileType.isFloor(tile.type))
            {
                return true; // We've hit a wall
            }
            error += deltay;
            if (error + error >= deltax)
            {
                y += ystep;
                error -= deltax;
            }
        }

        return false;
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay)
    {
        throw new UnsupportedOperationException();
    }
}
