package life.banana4.ld31;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ld31 extends ApplicationAdapter
{
    private Level level;
    private DrawContext drawContext;

    private Ld31Resources ld31Resources;

    @Override
    public void create()
    {
        this.ld31Resources = new Ld31Resources();
        ld31Resources.build();
        this.level = ld31Resources.levels.level1;
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        this.drawContext = new DrawContext(camera, new SpriteBatch(), new ShapeRenderer(), ld31Resources);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.level.render(drawContext, Gdx.graphics.getDeltaTime());
    }

    public Ld31Resources getLd31Resources()
    {
        return ld31Resources;
    }
}
