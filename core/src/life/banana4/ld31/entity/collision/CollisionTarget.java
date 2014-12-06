package life.banana4.ld31.entity.collision;

import com.badlogic.gdx.math.Vector2;

public interface CollisionTarget
{
    void onCollide(CollisionSource source, Vector2 mtv);
}
