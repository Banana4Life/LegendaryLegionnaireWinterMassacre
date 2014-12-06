package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;

public class Wall extends Entity
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
}
