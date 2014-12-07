package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class AbilityEntity extends Entity implements CollisionTarget
{
    public AbilityEntity()
    {
        super(4, 4);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {

    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        kill();
    }

    @Override
    public boolean acceptsCollisionsFrom(CollisionSource source)
    {
        return source instanceof Player;
    }
}
