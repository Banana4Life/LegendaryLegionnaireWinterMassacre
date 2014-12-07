package life.banana4.ld31.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.Projectile;

public class AlienProjectile extends Projectile
{
    public AlienProjectile(Entity shooter)
    {
        super(shooter, 8, 8);
        this.damagePotential = 1;
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        SpriteBatch b = ctx.getSpriteBatch();

        Texture tex = this.getLevel().getGame().getDrawContext().resources.textures.alienprojectile;
        Vector2 rot = new Vector2(-4, -4).rotate(getRotation() - 45);
        b.begin();
        b.draw(tex, getX() + getWidth() / 2 + rot.x, getY() + getHeight() / 2 + rot.y, 0, 0, 8, 8, 1, 1,
               getRotation() - 45, 0, 0, 8, 8, false, false);
        b.end();
    }

    @Override
    public float getSpeed()
    {
        return 500;
    }
}
