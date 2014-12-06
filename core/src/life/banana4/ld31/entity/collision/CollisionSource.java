package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Vector2;

public interface CollisionSource
{
    void onCollide(CollisionTarget target, Vector2 mtv);
}
