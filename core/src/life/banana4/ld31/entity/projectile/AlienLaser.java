package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.Projectile;

public class AlienLaser extends Projectile
{
    public AlienLaser(Entity shooter)
    {
        super(shooter, 1, 8, 8);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);

        if (lifetime() > 650)
        {
            kill();
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        SpriteBatch b = ctx.getSpriteBatch();

        Texture tex = this.getLevel().getGame().getDrawContext().resources.textures.alienprojectile;
        Vector2 rot = new Vector2(4, -4).rotate(getRotation() -90);
        b.begin();
        b.draw(tex, getMidX() + rot.x, getMidY() + rot.y, 0, 0, 8, 16, 1, 1,
               getRotation() + 90, 0, 0, 8, 16, false, false);
        b.end();
    }

    @Override
    public float getSpeed()
    {
        return 500;
    }
}
