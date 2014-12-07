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
    private int lastX = 0;
    private int lastY = 0;

    private int actualX = Gdx.input.getX();
    private int actualY = Gdx.input.getY();

    public Cursor()
    {
        super(0, 0);
        setDepth(Integer.MAX_VALUE);
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
        setPosition(lastX, lastY);
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {
        final int x = Gdx.input.getX();
        final int y = Gdx.input.getY();

        final int maxX = Gdx.graphics.getWidth() - 1;
        final int maxY = Gdx.graphics.getHeight() - 1;

        final int dx = x - lastX;
        final int dy = y - lastY;

        this.actualX = between(this.actualX + dx, 0, maxX);
        this.actualY = between(this.actualY - dy, 0, maxY);

        camera.project(POS.set(actualX, actualY, 0));

        setPosition(POS.x, POS.y);

        lastX = x;
        lastY = y;
    }

    private static int between(int a, int min, int max)
    {
        return Math.max(min, Math.min(max, a));
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
