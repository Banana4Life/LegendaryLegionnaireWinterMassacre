package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Rectangle;

public interface CollisionTarget
{
    void onCollide(Rectangle rect, CollisionSource source);
}
