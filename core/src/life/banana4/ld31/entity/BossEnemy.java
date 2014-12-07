package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;

public class BossEnemy extends Enemy
{
    private float stateTime = 0f;

    public BossEnemy()
    {
        super(45, 45);
        this.setHealth(20);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        stateTime += delta;
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch b = ctx.getSpriteBatch();
        b.begin();
        Vector2 v = new Vector2(64, -64).rotate(getViewingAngle());
        b.draw(ctx.resources.animations.unicornalien.getKeyFrame(stateTime, true), getMidX() + v.x, getMidY() + v.y, 0,
               0, 128, 128, 1, 1, getViewingAngle() + 90);
        b.end();
    }

    @Override
    public int getMaxHealth()
    {
        return 20;
    }
}
