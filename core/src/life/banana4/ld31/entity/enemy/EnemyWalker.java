package life.banana4.ld31.entity.enemy;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;

public class EnemyWalker extends Enemy
{
    private float stateTime = 0;

    public EnemyWalker()
    {
        super(25, 25);
    }

    public EnemyWalker(float damageDelay)
    {
        super(25, 25, damageDelay);
    }

    public EnemyWalker setStateTime(float stateTime)
    {
        this.stateTime = stateTime;
        return this;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        stateTime += delta;
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        getLevel().getGame().getDrawContext().resources.sounds.aliendeath.play();
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        SpriteBatch b = ctx.getSpriteBatch();
        b.begin();
        Vector2 v = new Vector2(64, -64).rotate(getViewingAngle());
        b.draw(ctx.resources.animations.alien.getKeyFrame(stateTime), getMidX() + v.x, getMidY() + v.y, 0, 0, 128,
               128, 1, 1, getViewingAngle() + 90);
        b.end();
    }
}
