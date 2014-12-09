package life.banana4.ld31.entity.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.entity.LivingEntity;
import life.banana4.ld31.util.TileType;

public class Unicorn extends Enemy
{
    private float stateTime = 0f;
    private boolean damaged = false;

    public Unicorn()
    {
        super(45, 45);
        this.setHealth(15);
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
        if (damaged)
        {
            damaged = false;
            b.setColor(Color.RED);
        }
        b.draw(ctx.resources.animations.unicornalien.getKeyFrame(stateTime), getMidX() + v.x, getMidY() + v.y,
                   0, 0, 128, 128, 1, 1, getViewingAngle() + 90);
        b.setColor(Color.WHITE);
        b.end();
    }

    @Override
    public int getMaxHealth()
    {
        return 15;
    }

    @Override
    public void onDeath()
    {
        super.onDeath();
        if (!getLevel().getPlayer().isDead())
        {
            if (getLevel().nodeAt(getX(), getY()).type != TileType.WALL)
            {
                this.getLevel().addEntity(new EnemyWalker().setStateTime(1f).move(getX(), getY()));
            }
        }
        getLevel().getGame().getDrawContext().resources.sounds.unicorndeath.play();
    }

    @Override
    public void melee(LivingEntity target)
    {
        super.melee(target);
        target.damage(4, this);
    }

    @Override
    public float getSpeed()
    {
        return 160;
    }

    @Override
    public int damage(int damage, LivingEntity e)
    {
        this.damaged = true;
        return super.damage(damage, e);
    }
}
