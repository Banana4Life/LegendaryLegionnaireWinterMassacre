package life.banana4.ld31.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;

public class Cursor extends Entity
{
    private static final Vector3 POS = new Vector3(0, 0, 0);

    public Cursor()
    {
        super(0, 0);
        setDepth(Integer.MAX_VALUE);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        camera.unproject(POS.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        setPosition(POS.x, POS.y);
    }

    @Override
    public void draw(DrawContext ctx, float delta)
    {
        super.draw(ctx, delta);

        ShapeRenderer r = ctx.getShapeRenderer();
        r.begin(ShapeType.Line);
        r.setColor(Color.RED);
        r.point(getX(), getY(), 0);
        r.circle(getX(), getY(), 3);
        r.circle(getX(), getY(), 6);
        r.circle(getX(), getY(), 9);
        r.circle(getX(), getY(), 12);
        r.end();

        Texture t = ctx.resources.textures.cursor;
        SpriteBatch b = ctx.getSpriteBatch();
        b.begin();
        b.draw(t, getX() - (t.getWidth() / 2f), getY() - (t.getHeight() / 2f));
        b.end();
    }
}
