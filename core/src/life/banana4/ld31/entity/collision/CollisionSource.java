package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Rectangle;

public interface CollisionSource
{
    void onCollide(Rectangle rect, CollisionTarget target);
}
