package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;

public class FootStep extends LivingEntity
{
    private final boolean left;

    public FootStep(boolean left)
    {
        super(0, 0);
        this.left = left;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        if (lifetime() > 5000)
        {
            kill();
        }
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        final Texture t = ctx.resources.textures.footstep;
        SpriteBatch b = ctx.getSpriteBatch();

        b.begin();
        Vector2 pos = new Vector2(0, left ? -5 : 5);
        pos.rotate(getRotation());
        b.draw(t, getX() - (t.getWidth() / 2) + pos.x, getY() + pos.y - (t.getHeight() / 2), 0, 0, t.getWidth(), t.getHeight(), 1, 1, getRotation() + 90, 0, 0, t.getWidth(), t.getHeight(), false, false);
        b.end();
    }
}
