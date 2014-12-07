package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import life.banana4.ld31.DrawContext;

public class AlienShip extends LivingEntity {

    public AlienShip(float width, float height)
    {
        super(width, height);
        setDepth(200);
        setPosition(100, 100);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch batch = ctx.getSpriteBatch();

        batch.begin();
        batch.draw(ctx.resources.textures.ship, getX(), getY(), 0, 0, 128, 128, 2, 2, 0, 0, 0, 128, 128, false, true);
        batch.end();
    }
}
