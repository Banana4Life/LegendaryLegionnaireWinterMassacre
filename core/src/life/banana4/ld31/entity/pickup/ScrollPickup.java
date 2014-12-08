package life.banana4.ld31.entity.pickup;

import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.entity.Particle;
import life.banana4.ld31.entity.Player;
import life.banana4.ld31.entity.collision.CollisionSource;

public class ScrollPickup extends Pickup
{
    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {
        if (source instanceof Player)
        {
            ((Player)source).addBombs(1);
        }
        kill();
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        texture = getLevel().getGame().getDrawContext().resources.textures.pickup2;
    }
}
