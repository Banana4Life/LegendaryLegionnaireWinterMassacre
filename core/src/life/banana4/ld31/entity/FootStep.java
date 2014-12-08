package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;

public class FootStep extends LivingEntity
{
    private final boolean left;
    private boolean reversed;

    public FootStep(boolean left)
    {
        super(2, 2);
        setDepth(25);
        this.left = left;
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        this.reversed = getLevel().getGame().getDrawContext().resources.animations.legs.getPlayMode() == PlayMode.REVERSED;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        super.update(camera, delta);
        if (lifetime() > 2000)
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
        Vector2 pos = new Vector2(8, left ? -16 : 0);
        pos.rotate(getRotation());
        b.draw(t, getX() + pos.x, getY() + pos.y, 0, 0, t.getWidth(), t.getHeight(), 1, 1, getRotation() + 90, 0, 0,
               t.getWidth(), t.getHeight(), false, reversed);
        b.end();
    }
}
