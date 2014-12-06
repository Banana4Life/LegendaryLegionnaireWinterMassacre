package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class Wall extends Entity implements CollisionTarget
{
    public Wall(float width, float height)
    {
        super(width, height);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {

    }

    @Override
    public void draw(DrawContext ctx)
    {
        super.draw(ctx);
        ShapeRenderer shapeRenderer = ctx.getShapeRenderer();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.OLIVE);
        shapeRenderer.box(this.getX(), this.getY(), 0, this.getWidth(), this.getHeight(), 0);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.box(this.getX(), this.getY(), 0, this.getWidth(), this.getHeight(), 0);
        shapeRenderer.end();
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {

    }
}
