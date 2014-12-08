package life.banana4.ld31.entity.projectile;

import life.banana4.ld31.entity.AlienShip;
import life.banana4.ld31.entity.Projectile;
import life.banana4.ld31.entity.collision.CollisionSource;

public class ShipLaser extends Projectile implements CollisionSource
{
    public ShipLaser(AlienShip ship)
    {
        super(ship, 10, 10);
    }

    @Override
    public float getSpeed()
    {
        return 1000;
    }
}
