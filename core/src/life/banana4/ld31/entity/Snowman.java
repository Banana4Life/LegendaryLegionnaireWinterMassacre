package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import life.banana4.ld31.DrawContext;

public class Snowman extends LivingEntity
{
    public Snowman()
    {
        super(0, 0);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        final SpriteBatch spriteBatch = ctx.getSpriteBatch();
        spriteBatch.begin();
        spriteBatch.draw(ctx.resources.textures.snowman, getX(), getY());
        spriteBatch.end();
    }
}
