package life.banana4.ld31;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DrawContext
{
    public final SpriteBatch spriteBatch;
    public final ShapeRenderer shapeRenderer;
    public final OrthographicCamera camera;

    public DrawContext(OrthographicCamera camera, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        this.camera = camera;
        this.spriteBatch = spriteBatch;
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch.setProjectionMatrix(camera.combined);
    }
}
