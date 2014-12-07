package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;

public class BossEnemy extends Enemy
{
    public BossEnemy()
    {
        super(45, 45);
        this.setHealth(20);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);
        ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(this.getMidX(), this.getMidY(), 22.5f);
        shapeRenderer.end();
    }

    @Override
    public int getMaxHealth()
    {
        return 20;
    }
}
