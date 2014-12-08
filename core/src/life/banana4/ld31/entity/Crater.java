package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;

public class Crater extends Entity
{
    public Crater(float x, float y) {
        super(64, 64);

        setPosition(x, y);
        setDepth(0);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();

        batch.begin();
        batch.draw(ctx.resources.textures.snowmanexplosion, getX() - 32, getY() - 32);
        batch.end();
    }
}
