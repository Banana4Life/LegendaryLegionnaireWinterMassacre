package life.banana4.ld31;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import life.banana4.ld31.input.GlobalInputProcessor;
import life.banana4.ld31.input.InputMultiplexer;
import life.banana4.ld31.resource.LevelLoader;

public class Ld31 extends ApplicationAdapter
{
    private static boolean DEBUG = System.getProperties().containsKey("debug");

    private Level level;
    private DrawContext drawContext;
    private Ld31Resources resources;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create()
    {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        inputMultiplexer = new InputMultiplexer(new GlobalInputProcessor(this, camera));
        Gdx.input.setInputProcessor(inputMultiplexer);
        Controllers.addListener(inputMultiplexer);
        Gdx.input.setCursorCatched(true);

        this.resources = new Ld31Resources();
        resources.build();
        this.drawContext = new DrawContext(camera, new SpriteBatch(), new ShapeRenderer(), resources);

        Pixmap cursor = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        Gdx.input.setCursorImage(cursor, cursor.getWidth() / 2, cursor.getHeight() / 2);

        if (!isDebug())
        {
            Music song = resources.songs.main;
            song.setLooping(true);
            song.setVolume(.1f);
            song.play();
        }
    }

    @Override
    public void render()
    {
        if (level == null)
        {
            level = LevelLoader.load(this, resources.textures);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.level.render(drawContext, Gdx.graphics.getDeltaTime());
    }

    public Level getLevel()
    {
        return level;
    }

    public DrawContext getDrawContext()
    {
        return drawContext;
    }

    public static boolean isDebug()
    {
        return DEBUG;
    }

    public InputMultiplexer getInputMultiplexer()
    {
        return inputMultiplexer;
    }

    public void reset()
    {
        if (level != null)
        {
            level.shutdown();
        }
        this.level = null;
    }
}
