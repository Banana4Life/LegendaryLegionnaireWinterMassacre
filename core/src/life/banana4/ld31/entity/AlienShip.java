package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.projectile.ShipLaser;

public class AlienShip extends LivingEntity
{

    private static final float SCALE = 2;
    private static final float ANGULAR_VELOCITY = 16;
    private final Snowman snowman;

    private boolean laserShot = false;

    public AlienShip(Snowman snowman)
    {
        super(128 * SCALE, 128 * SCALE);
        this.snowman = snowman;
        setDepth(200);
        setPosition(-getWidth(), 25);
        setVelocity(130, 0);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);

        if (getX() > 500 && !laserShot)
        {
            System.out.println("Shot!");
            this.laserShot = true;
            shoot(new ShipLaser(this), snowman.getMidX() - getMidX(), snowman.getMidY() - getMidY()).move(getMidX(),
                                                                                                          getMidY());
        }

        setRotation(getRotation() + delta * ANGULAR_VELOCITY);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();

        Texture t = ctx.resources.textures.ship;
        batch.begin();
        Vector2 pos = new Vector2(t.getWidth() * SCALE / 2, t.getHeight() * SCALE / 2);
        pos.rotate(getRotation());
        batch.draw(t, getMidX() - pos.x, getMidY() - pos.y, 0, 0, 128, 128, SCALE, SCALE, getRotation(), 0, 0, 128, 128,
                   false, true);
        batch.end();
    }

    @Override
    public void onOutsideWorld()
    {
        if (getX() > 0)
        {
            super.onOutsideWorld();
            System.out.println("Bye bye!");
        }
    }
}
