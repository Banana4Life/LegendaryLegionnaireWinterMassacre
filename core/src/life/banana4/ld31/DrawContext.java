package life.banana4.ld31;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DrawContext
{
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    public final OrthographicCamera camera;
    public final Ld31Resources resources;

    public DrawContext(OrthographicCamera camera, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer,
                       Ld31Resources resources)
    {
        this.camera = camera;
        this.spriteBatch = spriteBatch;
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.resources = resources;
    }

    public SpriteBatch getSpriteBatch()
    {
        spriteBatch.setProjectionMatrix(camera.combined);
        return spriteBatch;
    }

    public ShapeRenderer getShapeRenderer()
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        return shapeRenderer;
    }
}
